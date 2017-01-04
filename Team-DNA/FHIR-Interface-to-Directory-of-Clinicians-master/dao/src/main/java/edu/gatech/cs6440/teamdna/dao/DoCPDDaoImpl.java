package edu.gatech.cs6440.teamdna.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Repository;

import edu.gatech.cs6440.teamdna.Database;
import edu.gatech.cs6440.teamdna.impl.BaseDAO;
import edu.gatech.cs6440.teamdna.impl.DatabaseFactory;
import edu.gatech.cs6440.teamdna.model.DoCPDDo;

@Repository
public class DoCPDDaoImpl extends BaseDAO implements DoCPDDao, DoCPDDaoConstants{
	private static final Logger log = Logger.getLogger(DoCPDDaoImpl.class.getName());
	
	private static final String FIND_BY_BASE_SQL = "SELECT * FROM DBO." + TABLE_NAME + " WHERE ";
	private static final String FIND_BY_LICENSE_NO_SQL = FIND_BY_BASE_SQL + LICENSE_NO + " = ?";
	private static final String GET_BY_ID_SQL = FIND_BY_BASE_SQL + DoC_PD_ID + " = ?";
	private static final String DELETE_SQL = "DELETE FROM DBO." + TABLE_NAME + " WHERE " + ID + " = ?";

	private static final String INSERT_SQL = "INSERT INTO DBO." + TABLE_NAME + " ( " +
			ADDR_CITY + ", " +
			ADDR_COUNTY + ", " +
			ADDR_EMAIL + ", " +
			ADDR_LINE_1 + ", " +
			ADDR_LINE_2 + ", " +
			ADDR_PHONE + ", " +
			ADDR_STATE + ", " +
			ADDR_ZIPCODE + ", " +
			AKA + ", " +
			DATE_DECEASED + ", " +
			DATE_OF_BIRTH + ", " +
			DATE_THIS_STATUS + ", " +
			DISCIPLINE + ", " +
			DoC_PD_ID + ", " +
			EXPIRATION_DATE + ", " +
			FEDERAL_ID + ", " +
			FIRST_NAME + ", " +
			FULL_NAME + ", " +
			ISSUE_DATE + ", " +
			LAST_NAME + ", " +
			LICENSE_NAME + ", " +
			LICENSE_NAME2 + ", " +
			LICENSE_NO + ", " +
			LIC_STATUS + ", " +
			NAME_SUFFIX + ", " +
			PROFESSION_NAME + ", " +
			SORT_NAME + ", " +
			SSN + ") VALUES ( ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? )";

	private Database database;
	
	public DoCPDDaoImpl() {
		database = DatabaseFactory.getDatabase();
	}
	
	@Override
	public DoCPDDo getById(String id) {
		List<DoCPDDo> list = find(GET_BY_ID_SQL, new String[]{id});
		if(list == null || list.size() != 1){
			throw new IllegalArgumentException("No unique DoCPDDo record found for DoC_PD_ID=" + id);
		}
		
		return list.get(0);
	}
	
	@Override
	public List<DoCPDDo> findByLicenseNo(String licenseNo){
		return find(FIND_BY_LICENSE_NO_SQL, new String[]{licenseNo});
	}
	
	@Override
	public List<DoCPDDo> find(Map<String, List<String>> queryParams) {
		StringBuilder sql = new StringBuilder(FIND_BY_BASE_SQL);
		List<String> sqlParams = new ArrayList<String>();
		Set<String> keys = queryParams.keySet();
		for (String key : keys) {
			if(isColumn(key)){
				if(!sqlParams.isEmpty()){
					sql.append(" AND ");
				}
				List<String> values = queryParams.get(key);
				if(values.size() > 1){
					sql.append("( ");
					boolean first = true;
					for (String value : values) {
						if(!first){
							sql.append(" OR ");
						}
						first = false;
						sql.append(" UPPER(").append(key).append(") = ? ");
						sqlParams.add(value.toUpperCase());
					}
					sql.append(") ");
				} else {
					sqlParams.add(queryParams.get(key).get(0).toUpperCase());
					sql.append(" UPPER(").append(key).append(") = ? ");
				}				
			}
		}
		if(sqlParams.isEmpty()){
			return new ArrayList<DoCPDDo>();
		} 
		
		return find(sql.toString(), sqlParams.toArray(new String[sqlParams.size()]));
	}
	
	@Override
	public int delete(DoCPDDo docPD) {
		Connection conn = null;
		PreparedStatement stmnt = null;
		try {
			conn = database.getConnection();
			stmnt = conn.prepareStatement(DELETE_SQL);
			stmnt.setInt(1, docPD.getId());
			
			return stmnt.executeUpdate();
		} catch (SQLException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		} finally {
			close(stmnt, conn);
		}
		
		return 0;
	}
	
	public int save(DoCPDDo docPD){
		if(docPD == null){
			return 0;
		}
		
		if(docPD.getId() == null){
			return insert(docPD);
		} else {
			return update(docPD);
		}
	}
	
	private int update(DoCPDDo docPD) {
		// TODO Auto-generated method stub
		return 0;
	}

	private int insert(DoCPDDo docPD) {
		Connection conn = null;
		PreparedStatement stmnt = null;
		try {
			conn = database.getConnection();
			stmnt = conn.prepareStatement(INSERT_SQL);
			int i = 0;

//			stmnt.setInt(++i, docPD.getId());
			stmnt.setString(++i, docPD.getAddrCity());
			stmnt.setString(++i, docPD.getAddrCounty());
			stmnt.setString(++i, docPD.getAddrEmail());
			stmnt.setString(++i, docPD.getAddrLine1());
			stmnt.setString(++i, docPD.getAddrLine2());
			stmnt.setString(++i, docPD.getAddrPhone());
			stmnt.setString(++i, docPD.getAddrState());
			stmnt.setString(++i, docPD.getAddrZipcode());
			stmnt.setString(++i, docPD.getAka());
			stmnt.setString(++i, docPD.getDateDeceased());
			stmnt.setString(++i, docPD.getDateOfBirth());
			stmnt.setString(++i, docPD.getDateThisStatus());
			stmnt.setString(++i, docPD.getDiscipline());
			stmnt.setString(++i, docPD.getDocPdId());
			stmnt.setString(++i, docPD.getExpirationDate());
			stmnt.setString(++i, docPD.getFederalId());
			stmnt.setString(++i, docPD.getFirstName());
			stmnt.setString(++i, docPD.getFullName());
			stmnt.setString(++i, docPD.getIssueDate());
			stmnt.setString(++i, docPD.getLastName());
			stmnt.setString(++i, docPD.getLicenseName());
			stmnt.setString(++i, docPD.getLicenseName2());
			stmnt.setString(++i, docPD.getLicenseNo());
			stmnt.setString(++i, docPD.getLicStatus());
			stmnt.setString(++i, docPD.getNameSuffix());
			stmnt.setString(++i, docPD.getProfessionName());
			stmnt.setString(++i, docPD.getSortName());
			stmnt.setString(++i, docPD.getSsn());
			
			return stmnt.executeUpdate();
		} catch (SQLException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		} finally {
			close(stmnt, conn);
		}
		
		return 0;
	}
	
	private List<DoCPDDo> find(String sql, String[] params){
		List<DoCPDDo> docPDs = new ArrayList<DoCPDDo>();
		Connection conn = null;
		PreparedStatement stmnt = null;
		ResultSet rs = null;
		try {
			conn = database.getConnection();
			stmnt = conn.prepareStatement(sql);
			for (int i = 0; i < params.length; i++) {
				stmnt.setString(i + 1, params[i]);
			}
			
			rs = stmnt.executeQuery();
			while(rs.next()){
				DoCPDDo docPD = fromResultSet(rs);
				if(docPD != null){
					docPDs.add(docPD);
				}
			}
		} catch (SQLException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		} finally {
			close(rs, stmnt, conn);
		}
		
		return docPDs;
	}
	
	private DoCPDDo fromResultSet(ResultSet rs) throws SQLException{
		DoCPDDo docPD = null;
		if(rs != null){
			docPD = new DoCPDDo();
			docPD.setId(rs.getInt(ID));
			docPD.setAddrCity(rs.getString(ADDR_CITY));
			docPD.setAddrCounty(rs.getString(ADDR_COUNTY));
			docPD.setAddrEmail(rs.getString(ADDR_EMAIL));
			docPD.setAddrLine1(rs.getString(ADDR_LINE_1));
			docPD.setAddrLine2(rs.getString(ADDR_LINE_2));
			docPD.setAddrPhone(rs.getString(ADDR_PHONE));
			docPD.setAddrState(rs.getString(ADDR_STATE));
			docPD.setAddrZipcode(rs.getString(ADDR_ZIPCODE));
			docPD.setAka(rs.getString(AKA));
			docPD.setDateDeceased(rs.getString(DATE_DECEASED));
			docPD.setDateOfBirth(rs.getString(DATE_OF_BIRTH));
			docPD.setDateThisStatus(rs.getString(DATE_THIS_STATUS));
			docPD.setDiscipline(rs.getString(DISCIPLINE));
			docPD.setDocPdId(rs.getString(DoC_PD_ID));
			docPD.setExpirationDate(rs.getString(EXPIRATION_DATE));
			docPD.setFederalId(rs.getString(FEDERAL_ID));
			docPD.setFirstName(rs.getString(FIRST_NAME));
			docPD.setFullName(rs.getString(FULL_NAME));
			docPD.setIssueDate(rs.getString(ISSUE_DATE));
			docPD.setLastName(rs.getString(LAST_NAME));
			docPD.setLicenseName(rs.getString(LICENSE_NAME));
			docPD.setLicenseName2(rs.getString(LICENSE_NAME2));
			docPD.setLicenseNo(rs.getString(LICENSE_NO));
			docPD.setLicStatus(rs.getString(LIC_STATUS));
			docPD.setNameSuffix(rs.getString(NAME_SUFFIX));
			docPD.setProfessionName(rs.getString(PROFESSION_NAME));
			docPD.setSortName(rs.getString(SORT_NAME));
			docPD.setSsn(rs.getString(SSN));
		}
		
		return docPD;
	}
	
	private boolean isColumn(String name){
		if(name == null){
			return false;
		}
		return Arrays.asList(COLUMNS).contains(name.toUpperCase().trim());
	}
	
}
