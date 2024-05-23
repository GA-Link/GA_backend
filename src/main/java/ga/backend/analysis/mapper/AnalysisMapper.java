package ga.backend.analysis.mapper;

import ga.backend.analysis.dto.AnalysisResponseDto;
import ga.backend.analysis.entity.Analysis;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AnalysisMapper {
    @Mapping(source = "analysis", target = "customerConsultationRatio")
    @Mapping(source = "analysis", target = "taCustomerCount")
    @Mapping(source = "analysis", target = "scheduleCustomerCount")
    AnalysisResponseDto.Response analysisToAnalysisResponseDto(Analysis analysis);
}