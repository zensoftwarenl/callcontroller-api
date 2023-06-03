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

import zen.cybercloud.callcontroller.recordings.CDRitem;
import zen.cybercloud.callcontroller.recordings.RecordingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@Controller
@RequestMapping("/recordings")
public class RecordingController {

    private static final Logger LOG = LoggerFactory.getLogger(CallerIdController.class);

    @Autowired
    RecordingService recordingService;

    @Operation(summary = "Get info about recordings from a local CSV file")
    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Found the book",
//                    content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content)})
    @RequestMapping(value = "/csv", method = RequestMethod.GET,produces = "application/json")
    public @ResponseBody List<CDRitem> getInfo(
            @RequestParam(value = "dtfrom", required = false) String searchFrom,
            @RequestParam(value = "dtto", required = false) String searchTo,
            @RequestParam(value = "contactname", required = false) String searchContactName,
            @RequestParam(value = "contactphone", required = false) String searchContactPhone) throws IOException, ParseException {

//        LOG.info("recordinginfo:" + searchFrom + " - " +
//                                        searchTo + " - " +
//                                        searchContactName + " - " +
//                                        searchContactPhone + ";");
        var list = recordingService.downloadAndListCDRs();
        return list;
    }

//    @Operation(summary = "Get a book by its id")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Found the book",
//                    content = { @Content(mediaType = "application/json",
//                            schema = @Schema(implementation = Book.class)) }),
//            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
//                    content = @Content),
//            @ApiResponse(responseCode = "404", description = "Book not found",
//                    content = @Content) })

}
