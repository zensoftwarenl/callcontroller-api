/*
 * MIT License
 *
 * Copyright (c) 2023. ZEN Software B.V.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package zen.cybercloud.callcontroller.callerid;

import zen.cybercloud.callcontroller.recordings.CDRitem;
import zen.cybercloud.callcontroller.storage.GoogleCloudStorageService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CSVReaderHelper {
    private static final Logger LOG = LoggerFactory.getLogger(CSVReaderHelper.class);
    private static final String[] OUTLOOK_CONTACT_LOOKUP_LIST = {"First Name", "Middle Name", "Last Name", "Title",
            "Suffix", "Initials", "Web Page", "Gender", "Birthday", "Anniversary", "Location", "Language",
            "Internet Free Busy", "Notes", "E-mail Address", "E-mail 2 Address", "E-mail 3 Address", "Primary Phone",
            "Home Phone", "Home Phone 2", "Mobile Phone", "Pager", "Home Fax", "Home Address", "Home Street",
            "Home Street 2", "Home Street 3", "Home Address PO Box", "Home City", "Home State", "Home Postal Code",
            "Home Country", "Spouse", "Children", "Manager's Name", "Assistant's Name", "Referred By",
            "Company Main Phone", "Business Phone", "Business Phone 2", "Business Fax", "Assistant's Phone", "Company",
            "Job Title", "Department", "Office Location", "Organizational ID Number", "Profession", "Account",
            "Business Address", "Business Street", "Business Street 2", "Business Street 3", "Business Address PO Box",
            "Business City", "Business State", "Business Postal Code", "Business Country", "Other Phone", "Other Fax",
            "Other Address", "Other Street", "Other Street 2", "Other Street 3", "Other Address PO Box", "Other City",
            "Other State", "Other Postal Code", "Other Country", "Callback", "Car Phone", "ISDN", "Radio Phone",
            "TTY/TDD Phone", "Telex", "User 1", "User 2", "User 3", "User 4", "Keywords", "Mileage", "Hobby",
            "Billing Information", "Directory Server", "Sensitivity", "Priority", "Private", "Categories"};
    public static final int EMPTY_LINE_LENTGH = 3;
    //public static final int COMPANY_NAME_INDEX = 42;
    public static final int USER_FIELD_INDEX = 17;
    public static final int UNIQUE_ID_INDEX = 16;
    public static final int AMA_FLAGS_INDEX = 15;
    public static final int DISPOSITION_INDEX = 14;
    public static final int BILLSEC_INDEX = 13;
    public static final int DURATION_INDEX = 12;
    public static final int END_INDEX = 11;
    public static final int ANSWER_INDEX = 10;
    public static final int START_INDEX = 9;
    public static final int LAST_DATA_INDEX = 8;
    public static final int LAST_APP_INDEX = 7;
    public static final int DEST_CHANNEL_INDEX = 6;
    public static final int CHANNEL_INDEX = 5;
    public static final int CLID_INDEX = 4;
    public static final int DCONTEXT_INDEX = 3;
    public static final int DEST_INDEX = 2;
    public static final int SOURCE_INDEX = 1;
    public static final int ACCOUNT_CODE_INDEX = 0;

    @Autowired
    GoogleCloudStorageService googleCloudStorageService;

    BufferedReader getBufferedReaderForFile(String fileName) throws IOException {
        BufferedReader br;
        Pattern pattern = Pattern.compile("^gs://([^/]+)/(.*)$");
        Matcher matcher = pattern.matcher(fileName);

        if (matcher.find()) {
            String bucketName = matcher.group(1);
            String objectName = matcher.group(2);
            LOG.info("Downloading CSV file from Google Cloud Storage");
            var bytes = googleCloudStorageService.downloadFile(bucketName,objectName);
            br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bytes)));
        } else {
            LOG.info("Reading CSV file from classpath");
            br = new BufferedReader(new InputStreamReader(new ClassPathResource(fileName).getInputStream(), "cp1252"));
        }
        return br;
    }
    public List<Contact> parseWindowsContactsCSV(String contacts) {
        List<Contact> allContacts = new ArrayList<>();
        String line;
        String cvsSplitBy = ",";
        BufferedReader br = null;
        try {
            br = getBufferedReaderForFile(contacts);
            LOG.info("Parsing contacts CSV file");
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] lineArray = line.split(cvsSplitBy);

                if (lineArray.length < EMPTY_LINE_LENTGH) {
                    continue;
                }

                String myname = lineArray[0] + " " + lineArray[1] + " " + lineArray[2];
                Contact contact = new Contact.Builder().fullname(myname).build();

                // Pick company name if name is empty
//                if (myname.trim().length() == 0 && lineArray.length > COMPANY_NAME_INDEX) {
//                    myname = lineArray[COMPANY_NAME_INDEX];
//                }
                int idx = -1;
                for (String value : lineArray) {
                    idx++;
                    if (value == null || (value.length() == 0)) {
                        continue;
                    }

                    contact.setPart(OUTLOOK_CONTACT_LOOKUP_LIST[idx], value);
                }
                allContacts.add(contact);
                // log.info("Name " + lineArray[0] + " " + lineArray[1] + " "
                // + lineArray[2] + ";");

            }
        } catch (FileNotFoundException e) {
            LOG.info("File not found:" + contacts+ " Error: "+e.getMessage());
        } catch (IOException e) {
            LOG.info("IOException:" + contacts+ " Error: "+e.getMessage());
        } finally {
            if (br != null)
                try {
                    br.close();
                } catch (IOException e) {
                    LOG.error("IOException:" + contacts+ " Error: "+e.getMessage());
                }
        }
        return allContacts;
    }

    public List<CDRitem> readCDRMasterCSV(String contactsFileName) throws IOException {
        List<CDRitem> allContacts = new ArrayList<>();

        BufferedReader br;
        String line;

        br = getBufferedReaderForFile(contactsFileName);
        try {
            LOG.info("Parsing CDR CSV file");
            while ((line = br.readLine()) != null) {

                // use comma as separator and original split: line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                // split on comma but not in quotes
                final String[] lineArray = line.split(",(?=(?>[^\"]*\"[^\"]*\")*[^\"]*$)");
                LOG.info("Readline with:" + lineArray.length);
                if (lineArray.length < USER_FIELD_INDEX || line.startsWith("accountcode")){
                    LOG.warn("non parsable:" + line);
                    continue;
                }

                CDRitem item = new CDRitem.Builder()
                        .accountCode(lineArray[ACCOUNT_CODE_INDEX])
                        .src(lineArray[SOURCE_INDEX])
                        .dst(lineArray[DEST_INDEX])
                        .dcontext(lineArray[DCONTEXT_INDEX])
                        .clid(lineArray[CLID_INDEX])
                        .channel(lineArray[CHANNEL_INDEX])
                        .dstchannel(lineArray[DEST_CHANNEL_INDEX])
                        .lastapp(lineArray[LAST_APP_INDEX])
                        .lastdata(lineArray[LAST_DATA_INDEX])
                        .start(csvToParsableDate(lineArray[START_INDEX]))
                        .answer(csvToParsableDate(lineArray[ANSWER_INDEX]))
                        .end(csvToParsableDate(lineArray[END_INDEX]))
                        .duration(Integer.parseInt(lineArray[DURATION_INDEX].trim()))
                        .billsec(Integer.parseInt(lineArray[BILLSEC_INDEX].trim()))
                        .disposition(lineArray[DISPOSITION_INDEX])
                        .amaflags(lineArray[AMA_FLAGS_INDEX].trim())
                        .uniqueid(lineArray[UNIQUE_ID_INDEX])
                        .userfield(lineArray[USER_FIELD_INDEX])
                        .build();

                allContacts.add(item);
            }
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                LOG.error("IOException:" + contactsFileName+ " Error: "+e.getMessage());
            }
        }
        return allContacts;
    }

    private static Date csvToParsableDate(String fromcsv) {
        try {
            if (fromcsv != null && fromcsv.trim().length() > 0) {
                String noquotes = StringUtils.strip(fromcsv, "\"");
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.ENGLISH).parse(noquotes + " GMT+0:00");
            }
        } catch (ParseException e) {
            return null;
        }
        return null;
    }
}
