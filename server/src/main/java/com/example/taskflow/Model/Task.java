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

    public enum TaskStatus {
        TODO, IN_PROGRESS, DONE
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
                case TODO -> 0;
                case IN_PROGRESS -> 1;
                case DONE -> 2;
            };
        }

        @Override
        public TaskStatus convertToEntityAttribute(Integer code) {
            // DB -> Java direction: called on SELECT
            if (code == null) {
                return null;
            }
            return switch (code) {
                case 0 -> TaskStatus.TODO;
                case 1 -> TaskStatus.IN_PROGRESS;
                case 2 -> TaskStatus.DONE;
                default -> throw new IllegalArgumentException("Unknown code: " + code);
            };
        }
    }
}