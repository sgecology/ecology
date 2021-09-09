/**
 * 
 */
package net.ecology.marshal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import net.ecology.base.Marshaller;
import net.ecology.entity.scheduler.ScheduleJob;
import net.ecology.global.SchedulingConstants;

/**
 * @author ducbq
 *
 */
@Builder
@AllArgsConstructor
public class ScheduleJobMarshaller implements Marshaller<ScheduleJob, String[]> {
	@Override
	public ScheduleJob marshal(String[] elements) {
		return ScheduleJob.builder()
    .cronExpression(elements[SchedulingConstants.idx_JobCrontime])
    .jobClass(elements[SchedulingConstants.idx_JobClass])
    .code(elements[SchedulingConstants.idx_JobCode])
    .name(elements[SchedulingConstants.idx_JobName])
    .displayName(elements[SchedulingConstants.idx_JobDisplayName])
    .category(elements[SchedulingConstants.idx_JobCategory])
    .build();
	}

	@Override
	public String[] unmarshal(ScheduleJob repoObject) {
		// TODO Auto-generated method stub
		return null;
	}

}
