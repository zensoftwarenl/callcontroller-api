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

import zen.cybercloud.callcontroller.controllers.CallerIdController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VCFReaderHelper {
    /*
        Read a VCF contacts file exported from a contact app like Google Contacts
        process the file and return a list of Contact objects.
        Each contact object contains a map of key-value pairs with the contact parts
        like: name, phone, email, etc.
        The map key is the field name and the map value is the field value.
        The field name can be: name, phone, email, etc.
        The field value is the actual value of the field.
        For example:
        name: John Doe
        phone: +31123456789
        email: john.doe@gmail.com
     */
    private static final Logger LOG = LoggerFactory.getLogger(CallerIdController.class);

    public static List<Contact> readContactsVCF() throws IOException {
        File file = new File("contacts2023.vcf");
        List<Contact> contacts = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            Contact contact = null;
            while ((line = br.readLine()) != null) {
                if (line.contains("BEGIN:VCARD")) {
                    contact = new Contact.Builder().build();
                }
                if (line.contains("END:VCARD")) {
                    contacts.add(contact);
                }
                if (line.contains("FN:")) {
                    contact.setFullname(line.replace("FN:", ""));
                }
                if (line.contains("TEL;")) {
                    String[] parts = line.split(";");
                    String key = parts[0].replace("TEL", "");
                    String value = parts[1].replace(":", "");
                    contact.setPart(key, value);
                }
                if (line.contains("EMAIL;")) {
                    String[] parts = line.split(";");
                    String key = parts[0].replace("EMAIL", "");
                    String value = parts[1].replace(":", "");
                    contact.setPart(key, value);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return contacts;
    }

}
