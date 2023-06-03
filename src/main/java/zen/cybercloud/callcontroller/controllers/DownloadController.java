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

package zen.cybercloud.callcontroller.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Controller
@RequestMapping("/download")
public class DownloadController {

    private static final Logger LOG = LoggerFactory.getLogger(CallerIdController.class);

    @RequestMapping(value = "{fileName}", method = RequestMethod.GET)
    public void getRecording(@PathVariable String fileName, HttpServletResponse response) throws IOException {
        // todo: implement firestore storage and retrieval here
//        String path = "/var/spool/asterisk/monitor";
//        File dir = new File(path);
//        LOG.info("searching inside folder" + dir.getAbsolutePath());
//        String[] children = dir.list();
//        if (children != null) {
//            for (String aChildren : children) {
//                if (aChildren.equalsIgnoreCase(fileName)) {
//                    try {
//                        // get your file as InputStream
//                        InputStream is = new FileInputStream(new File(fileName));
//                        // copy it to response's OutputStream
//                        IOUtils.copy(is, response.getOutputStream());
//                        response.flushBuffer();
//                    } catch (IOException ex) {
//                        LOG.info("Error writing file to output stream. Filename was '" + fileName + "'");
//                        throw new RuntimeException("IOError writing file to output stream");
//                    }
//                }
//            }
//        }

    }
}
