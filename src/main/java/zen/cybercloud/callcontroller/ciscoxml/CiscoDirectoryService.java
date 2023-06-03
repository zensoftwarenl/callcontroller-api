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

package zen.cybercloud.callcontroller.ciscoxml;

import zen.cybercloud.callcontroller.callerid.Contact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CiscoDirectoryService {

    private static final Logger LOG = LoggerFactory.getLogger(CiscoDirectoryService.class);
    public static final int FIELD_LENGTH = 30;

    @Autowired
    private final ResourceHelper resourceHelper;

    @Value("${contacts.file}")
    private String contacts;

    @Autowired
    zen.cybercloud.callcontroller.callerid.CSVReaderHelper CSVReaderHelper;

    public CiscoDirectoryService(ResourceHelper resourceHelper) {
        this.resourceHelper = resourceHelper;
    }

    public CiscoMenu getIndex(String requestURL) {
        CiscoMenu everything = new CiscoMenu();
        everything.prompt = resourceHelper.getMessage("select.phonebook");
        everything.title = resourceHelper.getMessage("directoryname");
        everything.Menus = new ArrayList<>();
        CiscoMenuItem all = new CiscoMenuItem();
        all.Name = resourceHelper.getMessage("phonebook");
        all.URL = requestURL.replace("index", "directory?query=ALL&sidx=1");
        everything.Menus.add(all);
        CiscoMenuItem search = new CiscoMenuItem();
        search.Name = resourceHelper.getMessage("search");
        search.URL = requestURL.replace("index", "directorysearch");
        everything.Menus.add(search);
        LOG.info("Directory request index");
        return everything;
    }

    public CiscoDirectoryList getDirectory(Integer startWith, String searchFor, String servletPath)  {

        CiscoDirectoryList dirlist = new CiscoDirectoryList();
        LOG.info("Directory search request");

        dirlist.prompt = resourceHelper.getMessage("results") + " "
                + startWith + " " + resourceHelper.getMessage("to")
                + " " + (startWith + FIELD_LENGTH) + " "
                + resourceHelper.getMessage("for")
                + " " + searchFor;

        dirlist.title = resourceHelper.getMessage("phonebook.name");
        dirlist.ListOfEntries = new ArrayList<>();
        List<Contact> all = CSVReaderHelper.parseWindowsContactsCSV(contacts);
        int itemCount = 0;
        dirlist.ListOfKeys = new ArrayList<>();
        dirlist.ListOfKeys.add(CiscoSoftKeyItem.make(resourceHelper.getMessage("dial"), "1", "SoftKey:Dial"));
        dirlist.ListOfKeys.add(CiscoSoftKeyItem.make(resourceHelper.getMessage("editdial"), "2", "SoftKey:EditDial"));
        dirlist.ListOfKeys.add(CiscoSoftKeyItem.make(resourceHelper.getMessage("exit"), "3", "SoftKey:Exit"));

        for (Contact now : all) {
            itemCount++;
            if ((now.getFullname() == null) || (now.getFullname().length() == 0) || (itemCount < startWith)) {
                continue;
            }
            if ("ALL".equalsIgnoreCase(searchFor) || now.getFullname().toUpperCase().contains(searchFor.toUpperCase())) {
                for (Map.Entry<String, String> keyValuePair : now.getParts().entrySet()) {
                    if (keyValuePair.getValue() == null) {
                        continue;
                    }
                    String part = keyValuePair.getValue();

                    if (part.startsWith("+")) {
                        CiscoDirectoryItem newItem = new CiscoDirectoryItem();
                        newItem.Name = now.getFullname() + "(" + keyValuePair.getKey() + ")";
                        newItem.Telephone = part.replace("+31", "0");
                        dirlist.ListOfEntries.add(newItem);
                        if (dirlist.ListOfEntries.size() > FIELD_LENGTH) {
                            String nexturl = String.valueOf(servletPath);
                            if ((startWith - FIELD_LENGTH) > 0) {
                                CiscoSoftKeyItem back = new CiscoSoftKeyItem();
                                back.Name = resourceHelper.getMessage("previous");
                                back.Position = "4";
                                back.URL = nexturl.replace(servletPath, "/cisco/directory/?query="
                                        + searchFor + "&sidx=" + (startWith - FIELD_LENGTH));
                                dirlist.ListOfKeys.add(back);
                            }
                            CiscoSoftKeyItem next = new CiscoSoftKeyItem();
                            next.Name = resourceHelper.getMessage("next");
                            next.Position = "5";
                            next.URL = nexturl.replace(servletPath, "/cisco/directory?query="
                                    + searchFor + "&sidx=" + (startWith + FIELD_LENGTH));
                            dirlist.ListOfKeys.add(next);
                            return dirlist;
                        }
                    }
                }
            }
        }
        return dirlist;
    }

    public CiscoInputText searchDirectory(String servletPath, String requestURL) {

        LOG.info("Menu in XML path:" + servletPath);
        CiscoInputText search = new CiscoInputText.Builder().withPrompt(resourceHelper.getMessage("who.to.call"))
                .withTitle(resourceHelper.getMessage("search.phonebook"))
                .withPrompt(resourceHelper.getMessage("who.to.call"))
                .withURL(requestURL.replace(servletPath, "/directory/1"))
                .withInputItem(new CiscoInputItem.Builder().withDefaultValue("")
                        .withDisplayName(resourceHelper.getMessage("display.name"))
                        .withInputFlags("A")
                        .withQueryStringParam("query")
                        .build())
                .withTitle(resourceHelper.getMessage("search.phonebook"))
                .build();

        LOG.info("Search menu request");
        return search;
    }
}
