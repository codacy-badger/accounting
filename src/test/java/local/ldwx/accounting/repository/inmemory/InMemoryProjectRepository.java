package local.ldwx.accounting.repository.inmemory;

import local.ldwx.accounting.model.Project;
import local.ldwx.accounting.repository.ProjectRepository;
import local.ldwx.accounting.util.Util;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryProjectRepository implements ProjectRepository {

    private Map<Integer, Map<Integer, Project>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    @Override
    public Project save(Project project, int userId) {
        Map<Integer, Project> projects = repository.computeIfAbsent(userId, ConcurrentHashMap::new);
        if (project.isNew()) {
            project.setId(counter.incrementAndGet());
            projects.put(project.getId(), project);
            return project;
        }
        return projects.computeIfPresent(project.getId(), (id, oldProject) -> project);
    }

    @Override
    public boolean delete(int id, int userId) {
        Map<Integer, Project> projects = repository.get(userId);
        return projects != null && projects.remove(id) != null;
    }

    @Override
    public Project get(int id, int userId) {
        Map<Integer, Project> projects = repository.get(userId);
        return projects == null ? null : projects.get(id);
    }

    @Override
    public List<Project> getAll(int userId) {
        return getAllFiltered(userId, project -> true);
    }

    @Override
    public List<Project> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return getAllFiltered(userId, project -> Util.isBetween(project.getDateTime(), startDate, endDate));
    }

    private List<Project> getAllFiltered(int userId, Predicate<Project> filter) {
        Map<Integer, Project> projects = repository.get(userId);
        return CollectionUtils.isEmpty(projects) ? Collections.emptyList() :
                projects.values().stream()
                        .filter(filter)
                        .sorted(Comparator.comparing(Project::getDateTime).reversed())
                        .collect(Collectors.toList());
    }
}