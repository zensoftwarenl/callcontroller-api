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

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

//@XStreamAlias("CiscoIPPhoneMenu")
@XStreamAlias("CiscoIPPhoneMenu")
public class CiscoMenu extends CiscoAbstractTitleAndPrompt {
    /*
     * <CiscoIPPhoneMenu>
     *  <Title>Company Directory</Title>
     *  <Prompt>Please select an option</Prompt>
     *  <MenuItem>
     *    <Name>Bob Barker</Name>
     *    <URL>http://www.domain.com/person.php?Bob%20Barker</URL>
     *  </MenuItem>
     *
     * ... repeat as needed up to 31 entries
     *
     * <MenuItem>
     *  <Name>MORE</Name>
     *  <URL>http://www.domain.com/secondpage.php</URL>
     * </MenuItem>
     * </CiscoIPPhoneMenu>
     */

    /*
        <CiscoIPPhoneMenu>
            <Title>Franzen PBX Directory</Title>
            <Prompt>Selecteer een telefoonboek</Prompt>
            <MenuItem>
                <Name>Telefoonboek</Name>
                <URL>http://192.168.1.144:8080/cisco/directory/1?query=ALL</URL>
            </MenuItem>
            <MenuItem>
                <Name>Zoeken</Name>
                <URL>http://192.168.1.144:8080/cisco/directorysearch</URL>
            </MenuItem>
        </CiscoIPPhoneMenu>
     */

    @XStreamImplicit
    public List<CiscoMenuItem> Menus;

}
