package com.mycompany.application.module.controller;

import com.mycompany.application.module.controller.Error;
import com.mycompany.application.module.controller.Example;

import io.swagger.annotations.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import javax.validation.constraints.*;
import javax.validation.Valid;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-08-30T15:59:03.634+02:00")

@Controller
public class ExampleApiController implements ExampleApi {



    public ResponseEntity<List<Example>> exampleGet( @NotNull@ApiParam(value = "Field number one.", required = true) @RequestParam(value = "field1", required = true) Double field1) {
        // do some magic!
        return new ResponseEntity<List<Example>>(HttpStatus.OK);
    }

}
