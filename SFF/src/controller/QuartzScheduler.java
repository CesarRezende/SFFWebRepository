package controller;

import java.io.IOException;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.GenericServlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzScheduler extends GenericServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	EntityManagerFactory factory = null;

	public void init(ServletConfig config) throws ServletException {

		super.init(config);

		this.factory = Persistence.createEntityManagerFactory("SFF");

		Scheduler sched;
		try {

			sched = StdSchedulerFactory.getDefaultScheduler();
			sched.start();

			// 1 - Seconds, 2 - Minutes, 3 - Hours, 4 - Day-of-Month, 5 - Month,
			// 6 - Day-of-Week, 7 - Year (optional field)
			CronScheduleBuilder csbs = CronScheduleBuilder
					.cronSchedule("0 0 /6 1 * ?");
					//.cronSchedule("0 17 19 4 * ?");
			
			JobDetail jobs = JobBuilder.newJob(SaidaFixaJob.class)
					.withIdentity("SaidaFixaJob").build();
			
			jobs.getJobDataMap().put("entityManagerFactory", factory);

			Trigger triggers = TriggerBuilder.newTrigger()
					.withIdentity("SaidaFixaTrigger").withSchedule(csbs)
					.startNow().build();


			// 1 - Seconds, 2 - Minutes, 3 - Hours, 4 - Day-of-Month, 5 - Month,
			// 6 - Day-of-Week, 7 - Year (optional field)
			CronScheduleBuilder csbe = CronScheduleBuilder
					.cronSchedule("0 0 /6 1 * ?");
					//.cronSchedule("0 44 20 14 * ?");
			
			JobDetail jobe = JobBuilder.newJob(EntradaFixaJob.class)
					.withIdentity("EntradaFixaJob").build();
			jobe.getJobDataMap().put("entityManagerFactory", factory);
			
			Trigger triggere = TriggerBuilder.newTrigger()
					.withIdentity("EntradaFixaTrigger").withSchedule(csbe)
					.startNow().build();
			

			sched.scheduleJob(jobs, triggers);
			sched.scheduleJob(jobe, triggere);

		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	public void destroy() {

		this.factory.close();
	}

	@Override
	public void service(ServletRequest arg0, ServletResponse arg1)
			throws ServletException, IOException {

	}
}