package com.example.taskflow.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
public class Task {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String title;

    private String description;

    // autoApply = true on the converter below means Hibernate applies it to
    // every TaskStatus field automatically, so no @Convert annotation needed here.
    private TaskStatus status = TaskStatus.TODO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    // Many tasks -> one assignee. Optional: a task can start unassigned
    // (e.g. sitting in a backlog) and be picked up by a dev later.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dev_id", nullable = true)
    private Dev dev;

    public enum TaskStatus {
        UNASSIGNED, TODO, IN_PROGRESS, DONE
    }

    // static: converters are instantiated by Hibernate via a no-arg
    // constructor, with no Task instance available — a non-static inner
    // class can't be constructed that way and will fail at startup.
    @Converter(autoApply = true)
    public static class TaskStatusConverter implements AttributeConverter<TaskStatus, Integer> {

        @Override
        public Integer convertToDatabaseColumn(TaskStatus status) {
            // Java -> DB direction: called on INSERT/UPDATE
            if (status == null) {
                return null;
            }
            return switch (status) {
                case UNASSIGNED -> 0;
                case TODO -> 1;
                case IN_PROGRESS -> 2;
                case DONE -> 3;
            };
        }

        @Override
        public TaskStatus convertToEntityAttribute(Integer code) {
            // DB -> Java direction: called on SELECT
            if (code == null) {
                return null;
            }
            return switch (code) {
                case 0 -> TaskStatus.UNASSIGNED;
                case 1 -> TaskStatus.TODO;
                case 2 -> TaskStatus.IN_PROGRESS;
                case 3 -> TaskStatus.DONE;
                default -> throw new IllegalArgumentException("Unknown code: " + code);
            };
        }
    }
}