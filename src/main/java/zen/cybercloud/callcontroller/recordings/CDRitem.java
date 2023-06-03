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

package zen.cybercloud.callcontroller.recordings;

import java.util.Date;

// based on asterisk's cdr_csv module
public class CDRitem {

    // todo: consider Builder pattern implemented with apache commons
    //  https://commons.apache.org/proper/commons-lang/apidocs/org/apache/commons/lang3/builder/Builder.html

    // source: http://www.asteriskdocs.org/en/3rd_Edition/asterisk-book-html-chunk/asterisk-SysAdmin-SECT-1.html#default_cdr_fields

    // What account number to use: Asterisk billing account, (string, 20 characters)
    private final String accountcode;
    // Caller*ID number (string, 80 characters)
    private final String src;
    // Destination extension (string, 80 characters)
    private final String dst;
    // Destination context (string, 80 characters)
    private final String dcontext;
    // Caller*ID with text (80 characters)
    private final String clid;
    // Channel used (80 characters)
    private final String channel;
    // Destination channel if appropriate (80 characters)
    private final String dstchannel;
    // Last application if appropriate (80 characters)
    private final String lastapp;
    // Last application data (arguments) (80 characters)
    private final String lastdata;
    // Start of call (date/time)
    private final Date start;
    // Answer of call (date/time)
    private final Date answer;
    // End of call (date/time)
    private final Date end;
    // Total time in system, in seconds (integer)
    private final int duration;
    // Total time call is up, in seconds (integer)
    private final int billsec;
    // What happened to the call: ANSWERED, NO ANSWER, BUSY, FAILED
    private final String disposition;
    // What flags to use: see amaflags::DOCUMENTATION, BILL, IGNORE etc,
    // specified on a per-channel basis like accountcode.
    private final String amaflags;
    // A general-purpose user field. This field is empty by default and can be set to a user-defined string.[a]
    private final String userfield;
    // The unique ID for the src channel. This field is set automatically and is read-only.
    private final String uniqueid;


    public String getAccountcode() {
        return accountcode;
    }

    public String getSrc() {
        return src;
    }

    public String getDst() {
        return dst;
    }

    public String getDcontext() {
        return dcontext;
    }

    public String getClid() {
        return clid;
    }

    public String getChannel() {
        return channel;
    }

    public String getDstchannel() {
        return dstchannel;
    }

    public String getLastapp() {
        return lastapp;
    }

    public String getLastdata() {
        return lastdata;
    }

    public Date getStart() {
        return start;
    }

    public Date getAnswer() {
        return answer;
    }

    public Date getEnd() {
        return end;
    }

    public int getDuration() {
        return duration;
    }

    public int getBillsec() {
        return billsec;
    }

    public String getDisposition() {
        return disposition;
    }

    public String getAmaflags() {
        return amaflags;
    }

    public String getUserfield() {
        return userfield;
    }

    public String getUniqueid() {
        return uniqueid;
    }
    public static class Builder {
        private String accountcode;
        private String src;
        private String dst;
        private String dcontext;
        private String clid;
        private String channel;
        private String dstchannel;
        private String lastapp;
        private String lastdata;
        private Date start;
        private Date answer;
        private Date end;
        private int duration;
        private int billsec;
        private String disposition;
        private String amaflags;
        private String userfield;
        private String uniqueid;

        public Builder accountCode(String accountCode) {
            this.accountcode = accountCode;
            return this;
        }

        public Builder src(String src) {
            this.src = src;
            return this;
        }

        public Builder dst(String dst) {
            this.dst = dst;
            return this;
        }

        public Builder dcontext(String dcontext) {
            this.dcontext = dcontext;
            return this;
        }

        public Builder clid(String clid) {
            this.clid = clid;
            return this;
        }

        public Builder channel(String channel) {
            this.channel = channel;
            return this;
        }

        public Builder dstchannel(String dstchannel) {
            this.dstchannel = dstchannel;
            return this;
        }

        public Builder lastapp(String lastapp) {
            this.lastapp = lastapp;
            return this;
        }

        public Builder lastdata(String lastdata) {
            this.lastdata = lastdata;
            return this;
        }

        public Builder start(Date start) {
            this.start = start;
            return this;
        }

        public Builder answer(Date answer) {
            this.answer = answer;
            return this;
        }

        public Builder end(Date end) {
            this.end = end;
            return this;
        }

        public Builder duration(int duration) {
            this.duration = duration;
            return this;
        }

        public Builder billsec(int billsec) {
            this.billsec = billsec;
            return this;
        }

        public Builder disposition(String disposition) {
            this.disposition = disposition;
            return this;
        }

        public Builder amaflags(String amaflags) {
            this.amaflags = amaflags;
            return this;
        }

        public Builder userfield(String userfield) {
            this.userfield = userfield;
            return this;
        }


        public Builder uniqueid(String uniqueid) {
            this.uniqueid = uniqueid;
            return this;
        }

        public CDRitem build() {
            return new CDRitem(this);
        }

    }

    private CDRitem(Builder builder) {
        this.accountcode = builder.accountcode;
        this.src = builder.src;
        this.dst = builder.dst;
        this.dcontext = builder.dcontext;
        this.clid = builder.clid;
        this.channel = builder.channel;
        this.dstchannel = builder.dstchannel;
        this.lastapp = builder.lastapp;
        this.lastdata = builder.lastdata;
        this.start = builder.start;
        this.answer = builder.answer;
        this.end = builder.end;
        this.duration = builder.duration;
        this.billsec = builder.billsec;
        this.disposition = builder.disposition;
        this.amaflags = builder.amaflags;
        this.userfield = builder.userfield;
        this.uniqueid = builder.uniqueid;
    }
}
