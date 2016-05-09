package controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import data.RegistroRobo;
import data.SaidaFixa;
import business.ISaidaFixa;
import business.RegistroRoboRepository;
import business.SaidaFixaBean;
import business.SaidaFixaRepository;

public class SaidaFixaJob implements Job {

	public SaidaFixaJob() {
		// Instances of Job must have a public no-argument constructor.
	}

	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		Calendar now = Calendar.getInstance();
		SimpleDateFormat dataFormatada = new SimpleDateFormat(
				"dd/MM/yyyy HH:mm:ss", new Locale("pt", "BR"));
		RegistroRobo registroRobo = new RegistroRobo();
		registroRobo.setRoboNome("SaidaFixaJob");
		registroRobo.setAno(now.get(Calendar.YEAR));
		registroRobo.setMes(now.get(Calendar.MONTH) + 1);
		registroRobo.setDataExecucao(now.getTime());
		registroRobo.setObs("SaidaFixaJob Executado  = "
				+ dataFormatada.format(now.getTime()));

		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		EntityManagerFactory factory = (EntityManagerFactory) dataMap
				.get("entityManagerFactory");
		EntityManager entityManager = factory.createEntityManager();
		entityManager.getTransaction().begin();
		RegistroRoboRepository roboRepository = new RegistroRoboRepository();
		roboRepository.setEntityManager(entityManager);
		SaidaFixaRepository repository = new SaidaFixaRepository();
		repository.setEntityManager(entityManager);

		try {

			if (!roboRepository.existsRegistroRobo(registroRobo.getRoboNome(),
					registroRobo.getAno(), registroRobo.getMes())) {
				ISaidaFixa iSaidaFixa = new SaidaFixaBean();

				List<SaidaFixa> saidasFixa = repository
						.getSaidasFixasNotCanceled();

				for (Iterator<SaidaFixa> iterator = saidasFixa.iterator(); iterator
						.hasNext();) {
					SaidaFixa saidaFixa = (SaidaFixa) iterator.next();
					iSaidaFixa.generateMovimentacoesFinanc(saidaFixa, false);
					repository.saveSaidaFixa(saidaFixa);

				}

				now = Calendar.getInstance();
				registroRobo.setDataTermino(now.getTime());

				roboRepository.saveRegistroRobo(registroRobo);

				System.out.println("SaidaFixaJob Executado  = "
						+ dataFormatada.format(now.getTime()));
			}

			entityManager.getTransaction().commit();
		} catch (Exception e) {
			entityManager.getTransaction().rollback();
			e.printStackTrace();

		} finally {
			entityManager.close();
		}

	}

}
