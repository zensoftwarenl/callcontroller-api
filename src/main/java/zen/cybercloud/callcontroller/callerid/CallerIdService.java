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

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

@Service
public class CallerIdService {

    private static final Logger LOG = LoggerFactory.getLogger(CallerIdService.class);

    @Value("${contacts.file}")
    private String contacts;

    @Autowired
    CSVReaderHelper CSVReaderHelper;

    public String resolvePhoneNrPlainText(@PathVariable String dialedNumber) {
        List<Contact> all = CSVReaderHelper.parseWindowsContactsCSV(contacts);
        String name = "";
        String matchKey = "";
        for (Contact now : all) {
            for (Map.Entry<String, String> part : now.getParts().entrySet()) {
                if (part == null || (part.getValue() == null) || (part.getValue().length() == 0)) {
                    continue;
                }
                // matches +31123456 from phonebook to a sub-search (contains)
                // with prepending 0; 0123456 will match +31123456
                if (part.getValue().contains(StringUtils.strip(dialedNumber, "0")) && now.getFullname().trim().length() > 0) {
                    LOG.info("MATCH! for: " + now.getFullname());
                    name = now.getFullname();
                    matchKey = part.getKey();
                }
            }
        }

        LOG.info("Received incoming call from " + name + ":" + matchKey + "(" + StringUtils.strip(dialedNumber, "0") + ")");
        return name;

    }
}
