package business;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.Hibernate;
import org.json.JSONArray;

import data.Familia;
import data.MovimentacaoFinanceira;
import data.Usuario;

public class MovimentacaoFinanceiraRepository extends GenericRepository {

	public MovimentacaoFinanceiraRepository() {
		super();
	}
	
	public MovimentacaoFinanceiraRepository(HttpServletRequest request) {
		super(request);
	}

	public List<MovimentacaoFinanceira> getAllMovimentacaoFinanceira() {

		return super.getAll(MovimentacaoFinanceira.class);

	}

	public double getPreviewsBalance(Calendar lastDate, Calendar finalDate,
			Calendar today) {
		double previewsBalance = 0;
		Query query = null;
		Object result = null;

		updateEntityManager();

		query = this.entityManager
				.createQuery("Select Sum(m.valor - m.desconto + m.multa + m.juros) From MovimentacaoFinanceira m Inner Join m.usuario u  Where m.dataRealizada < :datarealizada and m.tipoMovimentacao =:tipomovimentacao and u.familia.id = :familiaid");
		query.setParameter("datarealizada", lastDate.getTime(),
				TemporalType.TIMESTAMP);
		query.setParameter("tipomovimentacao", 'C');
		query.setParameter("familiaid", this.usuario.getFamilia().getId());
		result = query.getSingleResult();

		if (result != null)
			previewsBalance = (Double) result;

		query = this.entityManager
				.createQuery("Select Sum(m.valor - m.desconto + m.multa + m.juros) From MovimentacaoFinanceira m Inner Join m.usuario u Where m.dataRealizada < :datarealizada and m.tipoMovimentacao =:tipomovimentacao and u.familia.id = :familiaid");
		query.setParameter("datarealizada", lastDate.getTime(),
				TemporalType.TIMESTAMP);
		query.setParameter("tipomovimentacao", 'D');
		query.setParameter("familiaid", this.usuario.getFamilia().getId());
		result = query.getSingleResult();

		if (result != null)
			previewsBalance -= (Double) result;

		int diffYear = finalDate.get(Calendar.YEAR)
				- today.get(Calendar.YEAR);
		int diffMonth = diffYear * 12 + finalDate.get(Calendar.MONTH)
				- today.get(Calendar.MONTH);

		if (diffMonth > 0) {

			query = this.entityManager
					.createQuery("Select Sum(m.valor - m.desconto + m.multa + m.juros) From MovimentacaoFinanceira m Inner Join m.usuario u Where (m.dataPrevista < :dataprevista  and m.dataRealizada IS NULL) and m.situacao=:situacao and m.tipoMovimentacao =:tipomovimentacao and u.familia.id = :familiaid");
			query.setParameter("dataprevista", lastDate.getTime(),
					TemporalType.TIMESTAMP);
			query.setParameter("tipomovimentacao", 'C');
			query.setParameter("situacao", 'P');
			query.setParameter("familiaid", this.usuario.getFamilia().getId());
			result = query.getSingleResult();

			if (result != null)
				previewsBalance += (Double) result;

			query = this.entityManager
					.createQuery("Select Sum(m.valor - m.desconto + m.multa + m.juros) From MovimentacaoFinanceira m Inner Join m.usuario u Where (m.dataPrevista < :dataprevista  and m.dataRealizada IS NULL) and m.situacao=:situacao and m.tipoMovimentacao =:tipomovimentacao and u.familia.id = :familiaid");
			query.setParameter("dataprevista", lastDate.getTime(),
					TemporalType.TIMESTAMP);
			query.setParameter("tipomovimentacao", 'D');
			query.setParameter("situacao", 'P');
			query.setParameter("familiaid", this.usuario.getFamilia().getId());
			result = query.getSingleResult();

			if (result != null)
				previewsBalance -= (Double) result;

		}

		return previewsBalance;
	}

	public double getMonthsSpendings(Calendar initialDate, Calendar finalDate,
			Calendar today) {
		double monthsSpendings = 0;
		Query query = null;
		Object result = null;

		updateEntityManager();

		int diffYear = finalDate.get(Calendar.YEAR)
				- today.get(Calendar.YEAR);
		int diffMonth = diffYear * 12 + finalDate.get(Calendar.MONTH)
				- today.get(Calendar.MONTH);

		if (diffMonth == 0) {

			query = this.entityManager
					.createQuery("Select Sum(m.valor - m.desconto + m.multa + m.juros) From MovimentacaoFinanceira m  Inner Join m.usuario u Where ((m.dataRealizada between :initialdate and :finaldate) or (m.dataPrevista <= :finaldate  and m.dataRealizada IS NULL)) and m.tipoMovimentacao =:tipomovimentacao and u.familia.id = :familiaid");
			query.setParameter("initialdate", initialDate.getTime(),
					TemporalType.TIMESTAMP);
			query.setParameter("finaldate", finalDate.getTime(),
					TemporalType.TIMESTAMP);
			query.setParameter("tipomovimentacao", 'D');
			query.setParameter("familiaid", this.usuario.getFamilia().getId());
			result = query.getSingleResult();

			if (result != null)
				monthsSpendings += (Double) result;

		} else if (diffMonth < 0) {

			query = this.entityManager
					.createQuery("Select Sum(m.valor - m.desconto + m.multa + m.juros) From MovimentacaoFinanceira m Inner Join m.usuario u Where ((m.dataRealizada between :initialdate and :finaldate)) and m.tipoMovimentacao =:tipomovimentacao and u.familia.id = :familiaid");
			query.setParameter("initialdate", initialDate.getTime(),
					TemporalType.TIMESTAMP);
			query.setParameter("finaldate", finalDate.getTime(),
					TemporalType.TIMESTAMP);
			query.setParameter("tipomovimentacao", 'D');
			query.setParameter("familiaid", this.usuario.getFamilia().getId());
			result = query.getSingleResult();

			if (result != null)
				monthsSpendings += (Double) result;

		} else {

			query = this.entityManager
					.createQuery("Select Sum(m.valor - m.desconto + m.multa + m.juros) From MovimentacaoFinanceira m Inner Join m.usuario u Where ((m.dataPrevista between :initialdate and :finaldate)) and m.situacao=:situacao  and m.tipoMovimentacao =:tipomovimentacao and u.familia.id = :familiaid");
			query.setParameter("initialdate", initialDate.getTime(),
					TemporalType.TIMESTAMP);
			query.setParameter("finaldate", finalDate.getTime(),
					TemporalType.TIMESTAMP);
			query.setParameter("tipomovimentacao", 'D');
			query.setParameter("situacao", 'P');
			query.setParameter("familiaid", this.usuario.getFamilia().getId());
			result = query.getSingleResult();

			if (result != null)
				monthsSpendings += (Double) result;

		}

		return monthsSpendings;
	}

	public double getCurrentMonthsSpendings(Calendar initialDate,
			Calendar finalDate, Calendar today) {
		double currentMonthsSpendings = 0;
		Query query = null;
		Object result = null;

		updateEntityManager();

		query = this.entityManager
				.createQuery("Select Sum(m.valor - m.desconto + m.multa + m.juros) From MovimentacaoFinanceira m  Inner Join m.usuario u Where ((m.dataRealizada between :initialdate and :finaldate)) and m.tipoMovimentacao =:tipomovimentacao and u.familia.id = :familiaid");
		query.setParameter("initialdate", initialDate.getTime(),
				TemporalType.TIMESTAMP);
		query.setParameter("finaldate", finalDate.getTime(),
				TemporalType.TIMESTAMP);
		query.setParameter("tipomovimentacao", 'D');
		query.setParameter("familiaid", this.usuario.getFamilia().getId());
		result = query.getSingleResult();

		if (result != null)
			currentMonthsSpendings += (Double) result;

		return currentMonthsSpendings;
	}

	public double getMonthsGains(Calendar initialDate, Calendar finalDate,
			Calendar today) {
		double monthsGains = 0;
		Query query = null;
		Object result = null;

		updateEntityManager();

		int diffYear = finalDate.get(Calendar.YEAR)
				- today.get(Calendar.YEAR);
		int diffMonth = diffYear * 12 + finalDate.get(Calendar.MONTH)
				- today.get(Calendar.MONTH);

		if (diffMonth == 0) {

			query = this.entityManager
					.createQuery("Select Sum(m.valor - m.desconto + m.multa + m.juros) From MovimentacaoFinanceira m  Inner Join m.usuario u Where ((m.dataRealizada between :initialdate and :finaldate) or (m.dataPrevista <= :finaldate  and m.dataRealizada IS NULL)) and m.tipoMovimentacao =:tipomovimentacao and u.familia.id = :familiaid");
			query.setParameter("initialdate", initialDate.getTime(),
					TemporalType.TIMESTAMP);
			query.setParameter("finaldate", finalDate.getTime(),
					TemporalType.TIMESTAMP);
			query.setParameter("tipomovimentacao", 'C');
			query.setParameter("familiaid", this.usuario.getFamilia().getId());
			result = query.getSingleResult();

			if (result != null)
				monthsGains += (Double) result;

		} else if (diffMonth < 0) {

			query = this.entityManager
					.createQuery("Select Sum(m.valor - m.desconto + m.multa + m.juros) From MovimentacaoFinanceira m  Inner Join m.usuario u Where ((m.dataRealizada between :initialdate and :finaldate)) and m.tipoMovimentacao =:tipomovimentacao and u.familia.id = :familiaid");
			query.setParameter("initialdate", initialDate.getTime(),
					TemporalType.TIMESTAMP);
			query.setParameter("finaldate", finalDate.getTime(),
					TemporalType.TIMESTAMP);
			query.setParameter("tipomovimentacao", 'C');
			query.setParameter("familiaid", this.usuario.getFamilia().getId());
			result = query.getSingleResult();

			if (result != null)
				monthsGains += (Double) result;

		} else {

			query = this.entityManager
					.createQuery("Select Sum(m.valor - m.desconto + m.multa + m.juros) From MovimentacaoFinanceira m  Inner Join m.usuario u Where ((m.dataPrevista between :initialdate and :finaldate)) and m.situacao=:situacao  and m.tipoMovimentacao =:tipomovimentacao and u.familia.id = :familiaid");
			query.setParameter("initialdate", initialDate.getTime(),
					TemporalType.TIMESTAMP);
			query.setParameter("finaldate", finalDate.getTime(),
					TemporalType.TIMESTAMP);
			query.setParameter("tipomovimentacao", 'C');
			query.setParameter("situacao", 'P');
			query.setParameter("familiaid", this.usuario.getFamilia().getId());
			result = query.getSingleResult();

			if (result != null)
				monthsGains += (Double) result;

		}

		return monthsGains;
	}
	
	public Object[] getMonthsGainsGroupByType(Calendar initialDate, Calendar finalDate,
			Calendar today) {
		Query query = null;
		Object[] results = null;

		updateEntityManager();

		int diffYear = finalDate.get(Calendar.YEAR)
				- today.get(Calendar.YEAR);
		int diffMonth = diffYear * 12 + finalDate.get(Calendar.MONTH)
				- today.get(Calendar.MONTH);

		if (diffMonth == 0) {

			query = this.entityManager
					.createQuery("Select Sum(CASE WHEN ev IS NOT NULL THEN (m.valor - m.desconto + m.multa + m.juros) ELSE 0 END)"
									+",Sum(CASE WHEN ef IS NOT NULL THEN (m.valor - m.desconto + m.multa + m.juros) ELSE 0 END)"
									+",Sum(CASE WHEN ef IS  NULL  AND ev IS  NULL THEN (m.valor - m.desconto + m.multa + m.juros) ELSE 0 END)"
									+" From MovimentacaoFinanceira m  Inner Join m.usuario u Left Join m.entradaVariavel ev Left Join m.entradaFixa ef "
									+"Where ((m.dataRealizada between :initialdate and :finaldate) or (m.dataPrevista <= :finaldate  and m.dataRealizada IS NULL)) and m.tipoMovimentacao =:tipomovimentacao and u.familia.id = :familiaid");
			query.setParameter("initialdate", initialDate.getTime(),
					TemporalType.TIMESTAMP);
			query.setParameter("finaldate", finalDate.getTime(),
					TemporalType.TIMESTAMP);
			query.setParameter("tipomovimentacao", 'C');
			query.setParameter("familiaid", this.usuario.getFamilia().getId());
			results = (Object[]) query.getSingleResult();



		} else if (diffMonth < 0) {

			query = this.entityManager
					.createQuery("Select Sum(CASE WHEN ev IS NOT NULL THEN (m.valor - m.desconto + m.multa + m.juros) ELSE 0 END)"
									+",Sum(CASE WHEN ef IS NOT NULL THEN (m.valor - m.desconto + m.multa + m.juros) ELSE 0 END)"
									+",Sum(CASE WHEN ef IS  NULL  AND ev IS  NULL THEN (m.valor - m.desconto + m.multa + m.juros) ELSE 0 END)"
									+"From MovimentacaoFinanceira m  Inner Join m.usuario u Left Join m.entradaVariavel ev Left Join m.entradaFixa ef "
									+"Where ((m.dataRealizada between :initialdate and :finaldate)) and m.tipoMovimentacao =:tipomovimentacao and u.familia.id = :familiaid");
			query.setParameter("initialdate", initialDate.getTime(),
					TemporalType.TIMESTAMP);
			query.setParameter("finaldate", finalDate.getTime(),
					TemporalType.TIMESTAMP);
			query.setParameter("tipomovimentacao", 'C');
			query.setParameter("familiaid", this.usuario.getFamilia().getId());
			results = (Object[]) query.getSingleResult();



		} else {

			query = this.entityManager
					.createQuery("Select Sum(CASE WHEN ev IS NOT NULL THEN (m.valor - m.desconto + m.multa + m.juros) ELSE 0 END)"
									+",Sum(CASE WHEN ef IS NOT NULL THEN (m.valor - m.desconto + m.multa + m.juros) ELSE 0 END)"
									+",Sum(CASE WHEN ef IS  NULL  AND ev IS  NULL THEN (m.valor - m.desconto + m.multa + m.juros) ELSE 0 END)"
									+"From MovimentacaoFinanceira m  Inner Join m.usuario u Left Join m.entradaVariavel ev Left Join m.entradaFixa ef "
									+"Where ((m.dataPrevista between :initialdate and :finaldate)) and m.situacao=:situacao  and m.tipoMovimentacao =:tipomovimentacao and u.familia.id = :familiaid");
			query.setParameter("initialdate", initialDate.getTime(),
					TemporalType.TIMESTAMP);
			query.setParameter("finaldate", finalDate.getTime(),
					TemporalType.TIMESTAMP);
			query.setParameter("tipomovimentacao", 'C');
			query.setParameter("situacao", 'P');
			query.setParameter("familiaid", this.usuario.getFamilia().getId());
			results = (Object[]) query.getSingleResult();



		}

		return results;
	}
	
	public Object[] getMonthsSpendingsGroupByType(Calendar initialDate, Calendar finalDate,
			Calendar today) {
		Query query = null;
		Object[] results = null;

		updateEntityManager();

		int diffYear = finalDate.get(Calendar.YEAR)
				- today.get(Calendar.YEAR);
		int diffMonth = diffYear * 12 + finalDate.get(Calendar.MONTH)
				- today.get(Calendar.MONTH);

		if (diffMonth == 0) {

			query = this.entityManager
					.createQuery("Select Sum(CASE WHEN sv IS NOT NULL THEN (m.valor - m.desconto + m.multa + m.juros) ELSE 0 END)"
									+",Sum(CASE WHEN sf IS NOT NULL THEN (m.valor - m.desconto + m.multa + m.juros) ELSE 0 END)"
									+",Sum(CASE WHEN sf IS  NULL  AND sv IS  NULL THEN (m.valor - m.desconto + m.multa + m.juros) ELSE 0 END)"
									+" From MovimentacaoFinanceira m  Inner Join m.usuario u Left Join m.saidaVariavel sv Left Join m.saidaFixa sf "
									+"Where ((m.dataRealizada between :initialdate and :finaldate) or (m.dataPrevista <= :finaldate  and m.dataRealizada IS NULL)) and m.tipoMovimentacao =:tipomovimentacao and u.familia.id = :familiaid");
			query.setParameter("initialdate", initialDate.getTime(),
					TemporalType.TIMESTAMP);
			query.setParameter("finaldate", finalDate.getTime(),
					TemporalType.TIMESTAMP);
			query.setParameter("tipomovimentacao", 'D');
			query.setParameter("familiaid", this.usuario.getFamilia().getId());
			results = (Object[]) query.getSingleResult();



		} else if (diffMonth < 0) {

			query = this.entityManager
					.createQuery("Select Sum(CASE WHEN sv IS NOT NULL THEN (m.valor - m.desconto + m.multa + m.juros) ELSE 0 END)"
									+",Sum(CASE WHEN sf IS NOT NULL THEN (m.valor - m.desconto + m.multa + m.juros) ELSE 0 END)"
									+",Sum(CASE WHEN sf IS  NULL  AND sv IS  NULL THEN (m.valor - m.desconto + m.multa + m.juros) ELSE 0 END)"
									+"From MovimentacaoFinanceira m  Inner Join m.usuario u Left Join m.saidaVariavel sv Left Join m.saidaFixa sf "
									+"Where ((m.dataRealizada between :initialdate and :finaldate)) and m.tipoMovimentacao =:tipomovimentacao and u.familia.id = :familiaid");
			query.setParameter("initialdate", initialDate.getTime(),
					TemporalType.TIMESTAMP);
			query.setParameter("finaldate", finalDate.getTime(),
					TemporalType.TIMESTAMP);
			query.setParameter("tipomovimentacao", 'D');
			query.setParameter("familiaid", this.usuario.getFamilia().getId());
			results = (Object[]) query.getSingleResult();



		} else {

			query = this.entityManager
					.createQuery("Select Sum(CASE WHEN sv IS NOT NULL THEN (m.valor - m.desconto + m.multa + m.juros) ELSE 0 END)"
									+",Sum(CASE WHEN sf IS NOT NULL THEN (m.valor - m.desconto + m.multa + m.juros) ELSE 0 END)"
									+",Sum(CASE WHEN sf IS  NULL  AND sv IS  NULL THEN (m.valor - m.desconto + m.multa + m.juros) ELSE 0 END)"
									+"From MovimentacaoFinanceira m  Inner Join m.usuario u Left Join m.saidaVariavel sv Left Join m.saidaFixa sf "
									+"Where ((m.dataPrevista between :initialdate and :finaldate)) and m.situacao=:situacao  and m.tipoMovimentacao =:tipomovimentacao and u.familia.id = :familiaid");
			query.setParameter("initialdate", initialDate.getTime(),
					TemporalType.TIMESTAMP);
			query.setParameter("finaldate", finalDate.getTime(),
					TemporalType.TIMESTAMP);
			query.setParameter("tipomovimentacao", 'D');
			query.setParameter("situacao", 'P');
			query.setParameter("familiaid", this.usuario.getFamilia().getId());
			results = (Object[]) query.getSingleResult();



		}

		return results;
	}
	
	public double getCurrentMonthsGains(Calendar initialDate, Calendar finalDate,
			Calendar today) {
		double currentMonthsGains = 0;
		Query query = null;
		Object result = null;

		updateEntityManager();


			query = this.entityManager
					.createQuery("Select Sum(m.valor - m.desconto + m.multa + m.juros) From MovimentacaoFinanceira m  Inner Join m.usuario u Where ((m.dataRealizada between :initialdate and :finaldate)) and m.tipoMovimentacao =:tipomovimentacao and u.familia.id = :familiaid");
			query.setParameter("initialdate", initialDate.getTime(),
					TemporalType.TIMESTAMP);
			query.setParameter("finaldate", finalDate.getTime(),
					TemporalType.TIMESTAMP);
			query.setParameter("tipomovimentacao", 'C');
			query.setParameter("familiaid", this.usuario.getFamilia().getId());
			result = query.getSingleResult();

			if (result != null)
				currentMonthsGains += (Double) result;

		return currentMonthsGains;
	}

	@SuppressWarnings("unchecked")
	public String TipoGastoChart(Calendar initialDate, Calendar finalDate,
			Calendar today) {
		updateEntityManager();

		Query query = null;
		@SuppressWarnings("rawtypes")
		List result = new ArrayList();
		JSONArray mJSONArray = new JSONArray(result);

		int diffYear = finalDate.get(Calendar.YEAR)
				- today.get(Calendar.YEAR);
		int diffMonth = diffYear * 12 + finalDate.get(Calendar.MONTH)
				- today.get(Calendar.MONTH);

		if (diffMonth == 0) {
			query = this.entityManager
					.createQuery("Select m.tipoGasto.descricao, Sum(m.valor - m.desconto + m.multa + m.juros) From MovimentacaoFinanceira  m  Inner Join m.usuario u Where ((m.dataRealizada between :initialdate and :finaldate) or (m.dataPrevista <= :finaldate  and m.dataRealizada IS NULL)) and m.tipoMovimentacao =:tipomovimentacao and u.familia.id = :familiaid Group by m.tipoGasto.descricao");
			query.setParameter("initialdate", initialDate.getTime(),
					TemporalType.TIMESTAMP);
			query.setParameter("finaldate", finalDate.getTime(),
					TemporalType.TIMESTAMP);
			query.setParameter("tipomovimentacao", 'D');
			query.setParameter("familiaid", this.usuario.getFamilia().getId());
			result = query.getResultList();

		} else if (diffMonth < 0) {
			query = this.entityManager
					.createQuery("Select m.tipoGasto.descricao, Sum(m.valor - m.desconto + m.multa + m.juros) From MovimentacaoFinanceira m  Inner Join m.usuario u Where (m.dataRealizada between :initialdate and :finaldate) and m.tipoMovimentacao =:tipomovimentacao and u.familia.id = :familiaid Group by m.tipoGasto.descricao");
			query.setParameter("initialdate", initialDate.getTime(),
					TemporalType.TIMESTAMP);
			query.setParameter("finaldate", finalDate.getTime(),
					TemporalType.TIMESTAMP);
			query.setParameter("tipomovimentacao", 'D');
			query.setParameter("familiaid", this.usuario.getFamilia().getId());
			result = query.getResultList();

		} else {
			query = this.entityManager
					.createQuery("Select m.tipoGasto.descricao, Sum(m.valor - m.desconto + m.multa + m.juros) From MovimentacaoFinanceira m Inner Join m.usuario u Where (m.dataPrevista between :initialdate and :finaldate) and m.situacao=:situacao and m.tipoMovimentacao =:tipomovimentacao and u.familia.id = :familiaid Group by m.tipoGasto.descricao");
			query.setParameter("initialdate", initialDate.getTime(),
					TemporalType.TIMESTAMP);
			query.setParameter("finaldate", finalDate.getTime(),
					TemporalType.TIMESTAMP);
			query.setParameter("tipomovimentacao", 'D');
			query.setParameter("situacao", 'P');
			query.setParameter("familiaid", this.usuario.getFamilia().getId());
			result = query.getResultList();

		}

		mJSONArray = new JSONArray(result);
		return mJSONArray.toString();
	}

	public ArrayList tipoGastoPeriodeChart(Calendar initialDate, Calendar finalDate,
			Calendar today) {
		updateEntityManager();

		Query query = null;
		@SuppressWarnings("rawtypes")
		List result = new ArrayList();

		int diffYear = finalDate.get(Calendar.YEAR)
				- today.get(Calendar.YEAR);
		int diffMonth = diffYear * 12 + finalDate.get(Calendar.MONTH)
				- today.get(Calendar.MONTH);

		if (diffMonth == 0) {
			query = this.entityManager
					.createQuery("Select m.tipoGasto.descricao, Sum(m.valor - m.desconto + m.multa + m.juros) From MovimentacaoFinanceira  m  Inner Join m.usuario u Where ((m.dataRealizada between :initialdate and :finaldate) or (m.dataPrevista <= :finaldate  and m.dataRealizada IS NULL)) and m.tipoMovimentacao =:tipomovimentacao and u.familia.id = :familiaid Group by m.tipoGasto.descricao");
			query.setParameter("initialdate", initialDate.getTime(),
					TemporalType.TIMESTAMP);
			query.setParameter("finaldate", finalDate.getTime(),
					TemporalType.TIMESTAMP);
			query.setParameter("tipomovimentacao", 'D');
			query.setParameter("familiaid", this.usuario.getFamilia().getId());
			result = query.getResultList();

		} else if (diffMonth < 0) {
			query = this.entityManager
					.createQuery("Select m.tipoGasto.descricao, Sum(m.valor - m.desconto + m.multa + m.juros) From MovimentacaoFinanceira m  Inner Join m.usuario u Where (m.dataRealizada between :initialdate and :finaldate) and m.tipoMovimentacao =:tipomovimentacao and u.familia.id = :familiaid Group by m.tipoGasto.descricao");
			query.setParameter("initialdate", initialDate.getTime(),
					TemporalType.TIMESTAMP);
			query.setParameter("finaldate", finalDate.getTime(),
					TemporalType.TIMESTAMP);
			query.setParameter("tipomovimentacao", 'D');
			query.setParameter("familiaid", this.usuario.getFamilia().getId());
			result = query.getResultList();

		} else {
			query = this.entityManager
					.createQuery("Select m.tipoGasto.descricao, Sum(m.valor - m.desconto + m.multa + m.juros) From MovimentacaoFinanceira m Inner Join m.usuario u Where (m.dataPrevista between :initialdate and :finaldate) and m.situacao=:situacao and m.tipoMovimentacao =:tipomovimentacao and u.familia.id = :familiaid Group by m.tipoGasto.descricao");
			query.setParameter("initialdate", initialDate.getTime(),
					TemporalType.TIMESTAMP);
			query.setParameter("finaldate", finalDate.getTime(),
					TemporalType.TIMESTAMP);
			query.setParameter("tipomovimentacao", 'D');
			query.setParameter("situacao", 'P');
			query.setParameter("familiaid", this.usuario.getFamilia().getId());
			result = query.getResultList();

		}

		return (ArrayList) result;
	}

	public List<MovimentacaoFinanceira> getListMovimentacaoFinanceira(
			Calendar initialDate, Calendar finalDate, Calendar today) {
		updateEntityManager();
		CriteriaBuilder criteriaBuilder = this.entityManager
				.getCriteriaBuilder();
		CriteriaQuery<MovimentacaoFinanceira> criteriaQuery = criteriaBuilder
				.createQuery(MovimentacaoFinanceira.class);
		Root<MovimentacaoFinanceira> root = criteriaQuery
				.from(MovimentacaoFinanceira.class);
		Join<MovimentacaoFinanceira,Usuario> joinUsr = root.join("usuario", JoinType.INNER);
		root.fetch("usuario", JoinType.INNER);
		root.join("tipoGasto", JoinType.LEFT);
		root.fetch("tipoGasto", JoinType.LEFT);
		root.fetch("saidaVariavel", JoinType.LEFT);
		root.fetch("entradaVariavel", JoinType.LEFT);
		criteriaQuery.select(root).distinct (true);
		

		List<Predicate> predicates = new ArrayList<Predicate>(2);

		int diffYear = finalDate.get(Calendar.YEAR)
				- today.get(Calendar.YEAR);
		int diffMonth = diffYear * 12 + finalDate.get(Calendar.MONTH)
				- today.get(Calendar.MONTH);

		if (diffMonth == 0) {

			predicates.add(criteriaBuilder.or(criteriaBuilder.and(
					criteriaBuilder.lessThanOrEqualTo(
							root.<Date> get("dataPrevista"),
							finalDate.getTime()),
					criteriaBuilder.isNull(root.<Date> get("dataRealizada"))),
					
					criteriaBuilder.between(root.<Date> get("dataRealizada"),
							initialDate.getTime(), finalDate.getTime())));
			
			predicates.add(criteriaBuilder.equal(joinUsr.<Familia> get("familia"),this.usuario.getFamilia()));

		} else if (diffMonth < 0) {
			predicates.add(criteriaBuilder.between(
					root.<Date> get("dataRealizada"), initialDate.getTime(),
					finalDate.getTime()));
			predicates.add(criteriaBuilder.equal(joinUsr.<Familia> get("familia"),this.usuario.getFamilia()));

		} else {
			predicates.add(criteriaBuilder.between(
					root.<Date> get("dataPrevista"), initialDate.getTime(),
					finalDate.getTime()));
			predicates.add(criteriaBuilder.equal(root.<Character>get("situacao"), 'P'));
			predicates.add(criteriaBuilder.equal(joinUsr.<Familia> get("familia"),this.usuario.getFamilia()));
		}

		criteriaQuery
				.where(predicates.toArray(new Predicate[predicates.size()]));

		TypedQuery<MovimentacaoFinanceira> query = this.entityManager
				.createQuery(criteriaQuery);
		List<MovimentacaoFinanceira> resultList = query.getResultList();

		return resultList;
	}

	public void saveMovimentacaoFinanceira(
			MovimentacaoFinanceira movimentacaoFinanceira) {

		super.save(movimentacaoFinanceira);
	}

	public void removeMovimentacaoFinanceira(
			MovimentacaoFinanceira movimentacaoFinanceira) {

		super.remove(movimentacaoFinanceira);
	}

	public void updateMovimentacaoFinanceira(
			MovimentacaoFinanceira movimentacaoFinanceira) {

		if(movimentacaoFinanceira.getEntradaVariavel() == null && movimentacaoFinanceira.getSaidaVariavel() == null)
			super.update(movimentacaoFinanceira);
		
		if(movimentacaoFinanceira.getSaidaVariavel() != null)
			new SaidaVariavelRepository().update(movimentacaoFinanceira.getSaidaVariavel());
		
		if(movimentacaoFinanceira.getEntradaVariavel() != null)
			new EntradaVariavelRepository().update(movimentacaoFinanceira.getEntradaVariavel());
	}
	
	public void updateMovimentacaoFinanceira(
			MovimentacaoFinanceira movimentacaoFinanceira, HttpServletRequest request) {

		if(movimentacaoFinanceira.getEntradaVariavel() == null && movimentacaoFinanceira.getSaidaVariavel() == null)
			super.update(movimentacaoFinanceira);
		
		if(movimentacaoFinanceira.getSaidaVariavel() != null)
			new SaidaVariavelRepository(request).update(movimentacaoFinanceira.getSaidaVariavel());
		
		if(movimentacaoFinanceira.getEntradaVariavel() != null)
			new EntradaVariavelRepository(request).update(movimentacaoFinanceira.getEntradaVariavel());
	}

	public MovimentacaoFinanceira findMovimentacaoFinanceira(Long id) {
		updateEntityManager();
		MovimentacaoFinanceira movimentFinanc = this.entityManager.find(MovimentacaoFinanceira.class, id);
		Hibernate.initialize(movimentFinanc.getSaidaVariavel());
		Hibernate.initialize(movimentFinanc.getEntradaVariavel());
		
		if(movimentFinanc.getSaidaVariavel() != null)
			Hibernate.initialize( movimentFinanc.getSaidaVariavel().getMovimentacoesfinanc());
		
		if(movimentFinanc.getEntradaVariavel() != null)
			Hibernate.initialize( movimentFinanc.getEntradaVariavel().getMovimentacoesfinanc());

		
		return movimentFinanc;
	}

}
