package com.zerock.sendbox.controller.board;

import com.zerock.sendbox.dto.board.ReplyDTO;
import com.zerock.sendbox.service.board.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/replies")
@Log4j2
@RequiredArgsConstructor
public class ReplyController {
    private final ReplyService replyService;

    @GetMapping(value = "/inquary/{inquaryNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReplyDTO>> getListByInquary(@PathVariable("inquaryNo") Integer inquaryNo) {
        log.info("inquaryNo: " + inquaryNo);
        return new ResponseEntity<>(replyService.getList(inquaryNo), HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> register(@RequestBody ReplyDTO replyDTO) {
        log.info(replyDTO);
        Integer replyNo = replyService.register(replyDTO);
        return new ResponseEntity<>(replyNo, HttpStatus.OK);
    }

    @DeleteMapping("/{replyNo}")
    public ResponseEntity<String> remove(@PathVariable("replyNo") Integer replyNo) {
        log.info("RNO: " + replyNo);
        replyService.remove(replyNo);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @PutMapping(value = "/{replyNo}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> modify(@PathVariable("replyNo") Integer replyNo, @RequestBody ReplyDTO replyDTO) {
        log.info(replyDTO);
        replyService.modify(replyDTO);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }
}
