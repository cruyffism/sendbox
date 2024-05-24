package com.zerock.sendbox.controller.board;

import com.zerock.sendbox.dto.board.AdminAnswerDTO;
import com.zerock.sendbox.service.board.AdminAnswerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/answer")
@Log4j2
@RequiredArgsConstructor
public class AdminAnswerController {

    private final AdminAnswerService adminAnswerService;

    @GetMapping(value = "/board/{boardNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AdminAnswerDTO>> getListByBoard(@PathVariable("boardNo") Integer boardNo) {
        log.info("boardNo " + boardNo);

        return new ResponseEntity<>(adminAnswerService.getLIst(boardNo), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Integer> register(@RequestBody AdminAnswerDTO adminAnswerDTO) {
        log.info(adminAnswerDTO);

        Integer answerNo = adminAnswerService.register(adminAnswerDTO);

        return new ResponseEntity<>(answerNo, HttpStatus.OK);
    }

    @DeleteMapping("/{answerNo}")
    public ResponseEntity<String> remove(@PathVariable("answerNo") Integer answerNo) {
        log.info("answerNo:" + answerNo);

        adminAnswerService.remove(answerNo);

        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @PutMapping("/{answerNo}")
    public ResponseEntity<String> modify(@RequestBody AdminAnswerDTO adminAnswerDTO) {
        log.info(adminAnswerDTO);

        adminAnswerService.modify(adminAnswerDTO);

        return new ResponseEntity<>("success", HttpStatus.OK);
    }

}
