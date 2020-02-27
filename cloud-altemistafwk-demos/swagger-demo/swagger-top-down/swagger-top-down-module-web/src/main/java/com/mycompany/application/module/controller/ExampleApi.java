/**
 * NOTE: This class is auto generated by the swagger code generator program (2.2.3).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package com.mycompany.application.module.controller;

import com.mycompany.application.module.controller.Error;
import com.mycompany.application.module.controller.Example;

import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import javax.validation.constraints.*;
import javax.validation.Valid;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-08-30T15:59:03.634+02:00")

@Api(value = "example", description = "the example API")
public interface ExampleApi {

    @ApiOperation(value = "Example Summary", notes = "This is a example of description ", response = Example.class, responseContainer = "List", tags={ "Example", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "An array", response = Example.class, responseContainer = "List"),
        @ApiResponse(code = 200, message = "Unexpected error", response = Error.class) })
    
    @RequestMapping(value = "/example",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<Example>> exampleGet( @NotNull@ApiParam(value = "Field number one.", required = true) @RequestParam(value = "field1", required = true) Double field1);

}
