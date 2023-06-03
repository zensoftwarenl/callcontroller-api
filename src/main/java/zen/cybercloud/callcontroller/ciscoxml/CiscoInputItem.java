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

@XStreamAlias("InputItem")
public class CiscoInputItem {

    public String DisplayName;

    public String QueryStringParam;

    public String DefaultValue;

    public String InputFlags;

    public static class Builder {
        private final CiscoInputItem value;

        public Builder() {
            value = new CiscoInputItem();
        }

        public Builder withDisplayName(String DisplayName) {
            value.DisplayName = DisplayName;
            return this;
        }

        public Builder withQueryStringParam(String QueryStringParam) {
            value.QueryStringParam = QueryStringParam;
            return this;
        }

        public Builder withDefaultValue(String DefaultValue) {
            value.DefaultValue = DefaultValue;
            return this;
        }

        public Builder withInputFlags(String InputFlags) {
            value.InputFlags = InputFlags;
            return this;
        }

        public CiscoInputItem build() {
            return value;
        }
    }
}

