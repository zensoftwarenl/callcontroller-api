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

@XStreamAlias("CiscoIPPhoneInput")
public class CiscoInputText extends CiscoAbstractTitleAndPrompt {
    /*
        <CiscoInputText>
            <Title>Doorzoek het telefoonboek</Title>
            <Prompt>wie wilt u bellen?</Prompt>
            <URL>http://<host>/directory/1</URL>
            <InputItem>
                <DisplayName>Naam</DisplayName>
                <QueryStringParam>query</QueryStringParam>
                <DefaultValue></DefaultValue>
                <InputFlags>A</InputFlags>
            </InputItem>
        </CiscoInputText>
     */

    public String URL;
    public CiscoInputItem InputItem;

    @XStreamImplicit()
    public List<CiscoSoftKeyItem> Softkeys;

    public CiscoInputText() {
        super();
    }

    public CiscoInputText(CiscoInputText rhs) {
        super(rhs);
        URL = rhs.URL;
        InputItem = rhs.InputItem;
        Softkeys = rhs.Softkeys;
    }

    public static class Builder extends CiscoAbstractTitleAndPrompt {
        private final CiscoInputText value;

        public Builder() {
            value = new CiscoInputText();
        }

        public Builder(CiscoInputText rhs) {
            value = new CiscoInputText(rhs);
        }

        public Builder withTitle(String title) {
            value.title = title;
            return this;
        }

        public Builder withPrompt(String prompt) {
            value.prompt = prompt;
            return this;
        }

        public Builder withURL(String url) {
            value.URL = url;
            return this;
        }

        public Builder withInputItem(CiscoInputItem inputItem) {
            value.InputItem = inputItem;
            return this;
        }

        public Builder withSoftkeys(List<CiscoSoftKeyItem> softkeys) {
            value.Softkeys = softkeys;
            return this;
        }

        public CiscoInputText build() {
            return value;
        }
    }


}
