package ru.ldwx.accounting.web.project;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.ldwx.accounting.model.Project;
import ru.ldwx.accounting.service.ProjectService;
import ru.ldwx.accounting.to.ProjectTo;
import ru.ldwx.accounting.util.DateTimeUtil;
import ru.ldwx.accounting.util.ProjectsUtil;
import ru.ldwx.accounting.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.ldwx.accounting.util.Util.orElse;
import static ru.ldwx.accounting.util.ValidationUtil.assureIdConsistent;
import static ru.ldwx.accounting.util.ValidationUtil.checkNew;

public abstract class AbstractProjectController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private ProjectService service;

    public Project get(int id) {
        int userId = SecurityUtil.authUserId();
        log.info("get project {} for user {}", id, userId);
        return service.get(id, userId);
    }

    public void delete(int id) {
        int userId = SecurityUtil.authUserId();
        log.info("delete project {} for user {}", id, userId);
        service.delete(id, userId);
    }

    public List<ProjectTo> getAll() {
        int userId = SecurityUtil.authUserId();
        log.info("getAll for user {}", userId);
        return ProjectsUtil.getWithExcess(service.getAll(userId), SecurityUtil.authUserSumPerDay());
    }

    public Project create(Project project) {
        int userId = SecurityUtil.authUserId();
        checkNew(project);
        log.info("create {} for user {}", project, userId);
        return service.create(project, userId);
    }

    public void update(Project project, int id) {
        int userId = SecurityUtil.authUserId();
        assureIdConsistent(project, id);
        log.info("update {} for user {}", project, userId);
        service.update(project, userId);
    }

    /**
     * <ol>Filter separately
     * <li>by date</li>
     * <li>by time for every date</li>
     * </ol>
     */
    public List<ProjectTo> getBetween(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        int userId = SecurityUtil.authUserId();
        log.info("getBetween dates({} - {}) time({} - {}) for user {}", startDate, endDate, startTime, endTime, userId);

        List<Project> mealsDateFiltered = service.getBetweenDates(
                orElse(startDate, DateTimeUtil.MIN_DATE), orElse(endDate, DateTimeUtil.MAX_DATE), userId);

        return ProjectsUtil.getFilteredWithExcess(mealsDateFiltered, SecurityUtil.authUserSumPerDay(),
                orElse(startTime, LocalTime.MIN), orElse(endTime, LocalTime.MAX)
        );
    }
}