package ga.backend.schedule.repository;

import ga.backend.customer.entity.Customer;
import ga.backend.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByCustomer(Customer customer);
}
