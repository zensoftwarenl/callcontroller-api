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

import zen.cybercloud.callcontroller.callerid.CSVReaderHelper;
import zen.cybercloud.callcontroller.storage.GoogleCloudStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@Service
public class RecordingService {

    @Autowired
    CSVReaderHelper CSVReaderHelper;

    @Autowired
    GoogleCloudStorageService googleCloudStorageService;

    @Value("${master.csv.file}")
    String masterCSVFile;
    public List<CDRitem> downloadAndListCDRs() throws IOException, ParseException {
        //        for(CDRitem call: list) {
//            log.info("Call:"+call.src + " - " + call.dst+ " - " + call.duration+ " - " + call.answer);
//        }
        //cloudStorageService.downloadFile()
        return CSVReaderHelper.readCDRMasterCSV(masterCSVFile);
    }
}
