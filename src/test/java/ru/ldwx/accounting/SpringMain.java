package ru.ldwx.accounting;

import org.springframework.context.support.GenericXmlApplicationContext;
import ru.ldwx.accounting.model.Role;
import ru.ldwx.accounting.model.User;
import ru.ldwx.accounting.to.ProjectTo;
import ru.ldwx.accounting.web.project.ProjectRestController;
import ru.ldwx.accounting.web.user.AdminRestController;


import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static ru.ldwx.accounting.TestUtil.mockAuthorize;
import static ru.ldwx.accounting.UserTestData.USER;


public class SpringMain {
    public static void main(String[] args) {
        // java 7 Automatic resource management
        try (GenericXmlApplicationContext appCtx = new GenericXmlApplicationContext()) {
            appCtx.getEnvironment().setActiveProfiles(Profiles.getActiveDbProfile(), Profiles.REPOSITORY_IMPLEMENTATION);
            appCtx.load("spring/spring-app.xml", "spring/inmemory.xml");
            appCtx.refresh();

            mockAuthorize(USER);

            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
            adminUserController.create(new User(null, "userName", "email@mail.ru", "password", 1500, Role.ROLE_ADMIN));
            System.out.println();

            ProjectRestController mealController = appCtx.getBean(ProjectRestController.class);
            List<ProjectTo> filteredMealsWithExceeded =
                    mealController.getBetween(
                            LocalDate.of(2015, Month.MAY, 30), LocalTime.of(7, 0),
                            LocalDate.of(2015, Month.MAY, 31), LocalTime.of(11, 0));
            filteredMealsWithExceeded.forEach(System.out::println);
        }
    }
}
