package ga.backend.analysis.controller;

import ga.backend.analysis.dto.AnalysisRequestDto;
import ga.backend.analysis.dto.AnalysisResponseDto;
import ga.backend.analysis.entity.Analysis;
import ga.backend.analysis.mapper.AnalysisMapper;
import ga.backend.analysis.service.AnalysisService;
import ga.backend.util.Version;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@RestController
@RequestMapping(Version.currentUrl + "/analysis")
@Validated
@AllArgsConstructor
public class AnalysisController {
    private final AnalysisService analysisService;
    private final AnalysisMapper analysisMapper;

    @GetMapping("/{date}")
    public ResponseEntity getCompanyList(
            @Valid @RequestBody AnalysisRequestDto.Get get,
            @PathVariable("date") String date) {
        System.out.println("### get : " + get.getDate());
        System.out.println("### get : " + LocalDate.parse(date));
        Analysis analysis = analysisService.findAnalysis(get.getDate());
        AnalysisResponseDto.Response response = analysisMapper.analysisToAnalysisResponseDto(analysis);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}