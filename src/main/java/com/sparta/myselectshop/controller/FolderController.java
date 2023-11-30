package com.sparta.myselectshop.controller;

import com.sparta.myselectshop.dto.FolderRequestDto;
import com.sparta.myselectshop.dto.FolderResponseDto;
import com.sparta.myselectshop.exception.RestApiException;
import com.sparta.myselectshop.security.UserDetailsImpl;
import com.sparta.myselectshop.service.FolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FolderController {

    private final FolderService folderService;
    @PostMapping("/folders")
    public void addFolders(@RequestBody FolderRequestDto folderRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        List<String> folderNames = folderRequestDto.getFolderNames();
        folderService.addFolders(folderNames, userDetails.getUser());
    }

    @GetMapping("/folders")
    public List<FolderResponseDto> getFolders(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return folderService.getFolders(userDetails.getUser());
    }

    // Controller에서 예외가 발생하면 호출이 되고 해당 예외를 처리를 해준다.
    // AOP 기반으로 작동해서 일일이 적용 시킬 필요 없다.
    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<RestApiException> handleException(IllegalArgumentException ex) {
        RestApiException restApiException = new RestApiException(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(
                // HTTP body
                restApiException,
                // HTTP status code
                HttpStatus.BAD_REQUEST
        );
    }
}
