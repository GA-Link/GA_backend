package ga.backend.analysis.service;

import ga.backend.analysis.entity.Analysis;
import ga.backend.analysis.repository.AnanlysisRepository;
import ga.backend.customer.entity.Customer;
import ga.backend.customer.repository.CustomerRepository;
import ga.backend.customer.service.CustomerService;
import ga.backend.customerType.entity.CustomerType;
import ga.backend.customerType.service.CustomerTypeService;
import ga.backend.employee.entity.Employee;
import ga.backend.exception.BusinessLogicException;
import ga.backend.exception.ExceptionCode;
import ga.backend.schedule.entity.Schedule;
import ga.backend.schedule.repository.ScheduleRepository;
import ga.backend.util.FindEmployee;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
@AllArgsConstructor
public class AnalysisService {
    private final AnanlysisRepository analysisRespository;
    private final CustomerRepository customerRepository;
    private final ScheduleRepository scheduleRepository;
    private final CustomerTypeService customerTypeService;
    private final FindEmployee findEmployee;

    // CREATE
    public Analysis createAnalysis(Analysis analysis) {
        return analysisRespository.save(analysis);
    }

    // READ
    public Analysis findAnalysis(long analysisPk) {
        Analysis analysis = verifiedAnalysis(analysisPk);
        return analysis;
    }

    public Analysis findAnalysis(LocalDate requestDate, long customerTypePk) {
        Employee employee = findEmployee.getLoginEmployeeByToken();
        LocalDate date = requestDate.withDayOfMonth(1); // 분석 날짜
        Analysis analysis = verifiedAnalysis(employee, date, customerTypePk); // 이전에 구현된 analysis가 있는지 확인

        // 분석하는 날짜
        LocalDateTime start = requestDate.atStartOfDay().withDayOfMonth(1); // 요청한 달의 첫째날 00:00:00
        LocalDateTime finish = requestDate.atTime(LocalTime.MAX).withDayOfMonth(requestDate.lengthOfMonth()); // 요청한 달의 마지막날 23:59:99

        // 계산여부 확인
        if(checkMonth(date)) { // 요청한 달이 이번달이면 계산

            analysisRespository.save(analysis);
        }

        return analysis;
    }

    // UPDATE
    public Analysis patchAnalysis(Analysis analysis) {
        Analysis findAnalysis = verifiedAnalysis(analysis.getPk());
//        Optional.ofNullable(analysis.getName()).ifPresent(findAnalysis::setName);

        return analysisRespository.save(findAnalysis);
    }

    // 검증
    public Analysis verifiedAnalysis(long analysisPk) {
        Optional<Analysis> analysis = analysisRespository.findById(analysisPk);
        return analysis.orElseThrow(() -> new BusinessLogicException(ExceptionCode.ANALYSIS_NOT_FOUND));
    }

    // 검증
    public Analysis verifiedAnalysis(Employee employee, LocalDate date, long customerTypePk) {
        CustomerType customerType = customerTypeService.findCustomerType(customerTypePk);

        Optional<Analysis> optionalAnalysis = analysisRespository.findByEmployeeAndDateAndCustomerType(
                employee,
                date,
                customerType
        );

        // requestDate가 존재하지 않은 경우
        return optionalAnalysis.orElseGet(() -> {
            Analysis analysis = new Analysis();
            analysis.setEmployee(employee); // 직원 설정
            analysis.setCustomerType(customerType); // 고객유형 설정
            analysis.setDate(date); // 성과분석 날짜 설정
            return analysis;
        });
    }

    // 요청 날짜가 이번달인지 확인
    // true : 요청날짜 == 이번달(다시 계산), false : 요청날짜 != 이번달(다시 계산X)
    public boolean checkMonth(LocalDate date) {
        boolean check = true;
        LocalDate now = LocalDate.now();

        if (now.getMonthValue() != date.getMonthValue()) check = false;
        else if (now.getYear() != date.getYear()) check = false;
        return check;
    }
}