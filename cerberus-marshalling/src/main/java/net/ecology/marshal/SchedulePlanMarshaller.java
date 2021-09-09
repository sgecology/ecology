/**
 * 
 */
package net.ecology.marshal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import net.ecology.base.Marshaller;
import net.ecology.common.CommonUtility;
import net.ecology.common.DateTimeUtility;
import net.ecology.entity.scheduler.SchedulePlan;
import net.ecology.global.SchedulingConstants;

/**
 * @author ducbq
 *
 */
@Builder
@AllArgsConstructor
public class SchedulePlanMarshaller implements Marshaller<SchedulePlan, String[]> {
	@Override
	public SchedulePlan marshal(String[] elements) {
		return SchedulePlan.builder()
		.code(elements[SchedulingConstants.idx_SP_Code])
    .name(elements[SchedulingConstants.idx_SP_Name])
    .startTime(CommonUtility.isEmpty(elements[SchedulingConstants.idx_SP_StartTime])?DateTimeUtility.getSystemDateTime():DateTimeUtility.parseDate(elements[SchedulingConstants.idx_SP_StartTime]))
    .jobType(elements[SchedulingConstants.idx_SP_JobType])
    .type(elements[SchedulingConstants.idx_SP_Type])
    .build(); 
	}

	@Override
	public String[] unmarshal(SchedulePlan repoObject) {
		// TODO Auto-generated method stub
		return null;
	}

}
