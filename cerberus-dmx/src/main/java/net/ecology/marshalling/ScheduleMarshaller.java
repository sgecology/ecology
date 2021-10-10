/**
 * 
 */
package net.ecology.marshalling;

import lombok.AllArgsConstructor;
import lombok.Builder;
import net.ecology.entity.scheduler.Schedule;
import net.ecology.global.SchedulingConstants;

/**
 * @author ducbq
 *
 */
@Builder
@AllArgsConstructor
public class ScheduleMarshaller extends MarshallerBasis<Schedule, String[]> {
	@Override
	public Schedule unmarshal(String[] elements) {
		return Schedule.builder()
    .cronExpression(elements[SchedulingConstants.idx_JobCrontime])
    .jobClass(elements[SchedulingConstants.idx_JobClass])
    .code(elements[SchedulingConstants.idx_JobCode])
    .name(elements[SchedulingConstants.idx_JobName])
    .displayName(elements[SchedulingConstants.idx_JobDisplayName])
    .category(elements[SchedulingConstants.idx_JobCategory])
    .build();
	}
}
