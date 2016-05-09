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

import data.EntradaFixa;
import data.RegistroRobo;
import business.IEntradaFixa;
import business.EntradaFixaBean;
import business.EntradaFixaRepository;
import business.RegistroRoboRepository;

public class EntradaFixaJob implements Job {

	public EntradaFixaJob() {
		// Instances of Job must have a public no-argument constructor.
	}

	public void execute(JobExecutionContext context)
			throws JobExecutionException {

		Calendar now = Calendar.getInstance();
		SimpleDateFormat dataFormatada = new SimpleDateFormat(
				"dd/MM/yyyy HH:mm:ss", new Locale("pt", "BR"));
		RegistroRobo registroRobo = new RegistroRobo();
		registroRobo.setRoboNome("EntradaFixaJob");
		registroRobo.setAno(now.get(Calendar.YEAR));
		registroRobo.setMes(now.get(Calendar.MONTH) + 1);
		registroRobo.setDataExecucao(now.getTime());
		registroRobo.setObs("EntradaFixaJob Executado  = "
				+ dataFormatada.format(now.getTime()));

		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		EntityManagerFactory factory = (EntityManagerFactory) dataMap
				.get("entityManagerFactory");
		EntityManager entityManager = factory.createEntityManager();
		entityManager.getTransaction().begin();
		RegistroRoboRepository roboRepository = new RegistroRoboRepository();
		roboRepository.setEntityManager(entityManager);
		EntradaFixaRepository repository = new EntradaFixaRepository();
		repository.setEntityManager(entityManager);

		try {
			
			if (!roboRepository.existsRegistroRobo(registroRobo.getRoboNome(),
					registroRobo.getAno(), registroRobo.getMes())) {

				IEntradaFixa iEntradaFixa = new EntradaFixaBean();

				List<EntradaFixa> entradasFixa = repository
						.getEntradasFixasNotCanceled();

				for (Iterator<EntradaFixa> iterator = entradasFixa.iterator(); iterator
						.hasNext();) {
					EntradaFixa entradaFixa = (EntradaFixa) iterator.next();
					iEntradaFixa
							.generateMovimentacoesFinanc(entradaFixa, false);
					repository.saveEntradaFixa(entradaFixa);

				}

				now = Calendar.getInstance();
				registroRobo.setDataTermino(now.getTime());
				
				roboRepository.saveRegistroRobo(registroRobo);
				
				System.out.println("EntradaFixaJob Executado  = "
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
