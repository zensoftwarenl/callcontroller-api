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

import zen.cybercloud.callcontroller.ciscoxml.CiscoDirectoryList;
import zen.cybercloud.callcontroller.ciscoxml.CiscoDirectoryService;
import zen.cybercloud.callcontroller.ciscoxml.CiscoInputText;
import zen.cybercloud.callcontroller.ciscoxml.CiscoMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/cisco")
public class CiscoDirectoryController {

    @Autowired
    CiscoDirectoryService ciscoDirectoryService;

    @Autowired
    zen.cybercloud.callcontroller.callerid.CSVReaderHelper CSVReaderHelper;

    @RequestMapping(value = "/index", method = RequestMethod.GET, produces = "text/xml")
    @ResponseBody
    public CiscoMenu getIndex(HttpServletRequest request) {
        return ciscoDirectoryService.getIndex(request.getRequestURL().toString());
    }

    @RequestMapping(value = "/directory", method = RequestMethod.GET, produces = "text/xml")
    @ResponseBody
    public CiscoDirectoryList getDirectory(HttpServletRequest request,
                                           @RequestParam(value = "sidx") Integer startWith,
                                           @RequestParam(value = "query") String searchFor) {
        return ciscoDirectoryService.getDirectory(startWith, searchFor,request.getServletPath() );
    }

    @RequestMapping(value = "/directorysearch", method = RequestMethod.GET, produces = "text/xml")
    public @ResponseBody
    CiscoInputText searchDirectory(HttpServletRequest request) {
        return ciscoDirectoryService.searchDirectory(request.getServletPath(), String.valueOf(request.getRequestURL()));
    }
}
