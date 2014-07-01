package org.wso2.carbon.identity.certificateauthority.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.base.IdentityException;
import org.wso2.carbon.identity.certificateauthority.Constants;
import org.wso2.carbon.identity.certificateauthority.crl.RevokedCertInfo;
import org.wso2.carbon.identity.certificateauthority.data.CertAuthException;
import org.wso2.carbon.identity.certificateauthority.data.RevokedCertificate;
import org.wso2.carbon.identity.core.persistence.JDBCPersistenceManager;
import org.wso2.carbon.identity.core.util.IdentityDatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class RevocationDAO {
    Log log = LogFactory.getLog(RevocationDAO.class);

    /**
     * add revoked certificate to the database
     *
     * @param serialNo serialNo of the revoked certificate
     * @param tenantID
     * @param reason   reason for the revoke
     * @throws CertAuthException
     */

    public void addRevokedCert(String serialNo, int tenantID, int reason) throws CertAuthException {
        Connection connection = null;
        Date requestDate = new Date();
        String sql = null;
        PreparedStatement prepStmt = null;
        try {
            log.debug("adding revoked certificate to database");
            connection = JDBCPersistenceManager.getInstance().getDBConnection();
            sql = "INSERT INTO CA_REVOKED_CERTIFICATES (SERIAL_NO, REVOKED_DATE, TENANT_ID, REASON) VALUES (?,?,?,?) ";
            prepStmt = connection.prepareStatement(sql);
            prepStmt.setString(1, serialNo);
            prepStmt.setDate(2, new java.sql.Date(new Date().getTime()));
            prepStmt.setInt(3, tenantID);
            prepStmt.setInt(4, reason);
            prepStmt.execute();
            connection.commit();
        } catch (IdentityException e) {
            String errorMsg = "Error when getting an Identity Persistence Store instance.";
            log.error(errorMsg, e);
            throw new CertAuthException(errorMsg, e);
        } catch (SQLException e) {
            log.error("Error when executing the SQL : " + sql);
            log.error(e.getMessage(), e);
        } finally {
            IdentityDatabaseUtil.closeAllConnections(connection, null, prepStmt);
        }
    }

    /**
     * get Revoked certificate from serial number
     *
     * @param serialNo
     * @return RevokedCertificate with given serial number
     * @throws CertAuthException
     */
    public RevokedCertificate getRevokedCertificate(String serialNo) throws CertAuthException {
        Connection connection = null;
        PreparedStatement prepStmt = null;
        ResultSet resultSet;
        String sql = null;

        try {
            log.debug("retriving revoked certificate from serial :" + serialNo);
            connection = JDBCPersistenceManager.getInstance().getDBConnection();
            sql = "SELECT * FROM CA_REVOKED_CERTIFICATES WHERE SERIAL_NO = ?";
            prepStmt = connection.prepareStatement(sql);

            prepStmt.setString(1, serialNo);
            resultSet = prepStmt.executeQuery();
            RevokedCertificate[] revCerts = getRevCertArray(resultSet);
            if (revCerts != null && revCerts.length > 0) {
                return revCerts[0];
            }
        } catch (IdentityException e) {
            String errorMsg = "Error when getting an Identity Persistence Store instance.";
            log.error(errorMsg, e);
            throw new CertAuthException(errorMsg, e);
        } catch (SQLException e) {
            log.error("Error when executing the SQL : " + sql);
            log.error(e.getMessage(), e);
        } finally {
            IdentityDatabaseUtil.closeAllConnections(connection, null, prepStmt);
        }
        return null;
    }

    /**
     * get RevokedCertificateArray from resultset
     *
     * @param resultSet
     * @return
     */
    public RevokedCertificate[] getRevCertArray(ResultSet resultSet) {
        ArrayList<RevokedCertificate> revokedList = new ArrayList<RevokedCertificate>();
        int count = 0;
        try {
            while (resultSet.next()) {
                String serialNo = resultSet.getString(Constants.SERIAL_NO_LABEL);
                int reason = resultSet.getInt(Constants.RC_REV_REASON);
                Date revokedDate = resultSet.getDate(Constants.RC_REV_DATE_LABEL);
                RevokedCertificate revCertificate = new RevokedCertificate(serialNo, revokedDate, reason);
                revokedList.add(revCertificate);
            }
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        RevokedCertificate[] revokedCerts = new RevokedCertificate[revokedList.size()];
        revokedCerts = revokedList.toArray(revokedCerts);

        return revokedCerts;


    }

    /**
     * delete revoked certificate from the database
     *
     * @param serialNo
     * @return
     * @throws CertAuthException
     */
    public int deleteRevokedCert(String serialNo) throws CertAuthException {
        Connection connection = null;
        PreparedStatement prepStmt = null;
        int result = 0;
        String sql = null;
        try {
            log.debug("deleting revoked certificate with serial number :" + serialNo);
            connection = JDBCPersistenceManager.getInstance().getDBConnection();
            sql = "DELETE FROM CA_REVOKED_CERTIFICATES WHERE SERIAL_NO= ?";
            prepStmt = connection.prepareStatement(sql);

            prepStmt.setString(1, serialNo);
            result = prepStmt.executeUpdate();
            connection.commit();
            if (result == 1) {
                log.debug("revoked certificate with serial number " + serialNo + "is deleted");
            } else {
                log.debug("error while deleting revoked certificaate with serial number " + serialNo);
            }
        } catch (IdentityException e) {
            String errorMsg = "Error when getting an Identity Persistence Store instance.";
            log.error(errorMsg, e);
            throw new CertAuthException(errorMsg, e);
        } catch (SQLException e) {
            log.error("Error when executing the SQL : " + sql);
            log.error(e.getMessage(), e);
        } finally {
            IdentityDatabaseUtil.closeAllConnections(connection, null, prepStmt);
        }
        return result;
    }

    /**
     * get revoked certificate by a tenant
     *
     * @param
     * @return
     * @throws CertAuthException
     */
    public RevokedCertificate[] getRevokedCerts(int tenantId) throws CertAuthException {
        Connection connection = null;
        PreparedStatement prepStmt = null;
        ResultSet resultSet;
        String sql = null;
        try {
            log.debug("retriving revoked certificate from serial :" + tenantId);
            connection = JDBCPersistenceManager.getInstance().getDBConnection();
            sql = "SELECT * FROM CA_REVOKED_CERTIFICATES WHERE TENANT_ID = ?";
            prepStmt = connection.prepareStatement(sql);

            prepStmt.setInt(1, tenantId);

            resultSet = prepStmt.executeQuery();
            RevokedCertificate[] revCerts = getRevCertArray(resultSet);
            return revCerts;

        } catch (IdentityException e) {
            String errorMsg = "Error when getting an Identity Persistence Store instance.";
            log.error(errorMsg, e);
            throw new CertAuthException(errorMsg, e);
        } catch (SQLException e) {
            log.error("Error when executing the SQL : " + sql);
            log.error(e.getMessage(), e);
        } finally {
            IdentityDatabaseUtil.closeAllConnections(connection, null, prepStmt);
        }
        return null;
    }

    /**
     * remove all actived certificates from revocation table
     *
     * @throws CertAuthException
     */
    public void removeActivedCerts() throws CertAuthException {
        Connection connection = null;
        PreparedStatement prepStmt = null;
        ResultSet resultSet;
        String sql = null;

        try {
            log.debug("deleting re actived certs from Revoked table");
            connection = JDBCPersistenceManager.getInstance().getDBConnection();
            sql = "DELETE FROM CA_REVOKED_CERTIFICATES WHERE REASON = ?";
            prepStmt = connection.prepareStatement(sql);
            prepStmt.setInt(1, RevokedCertInfo.REVOCATION_REASON_REMOVEFROMCRL);
            prepStmt.executeUpdate();
        } catch (IdentityException e) {
            String errorMsg = "Error when getting an Identity Persistence Store instance.";
            log.error(errorMsg, e);
            throw new CertAuthException(errorMsg, e);
        } catch (SQLException e) {
            log.error("Error when executing the SQL : " + sql);
            log.error(e.getMessage(), e);
        } finally {
            IdentityDatabaseUtil.closeAllConnections(connection, null, prepStmt);
        }

    }

    /**
     * returns revoked certificates after the given date
     *
     * @param tenantId id of the tenant
     * @param date     date to be compared
     * @return set of revoked certificates which are revoked after the given date
     * @throws CertAuthException
     */
    public RevokedCertificate[] getRevokedCertsAfter(int tenantId, Date date) throws CertAuthException {
        Connection connection = null;
        PreparedStatement prepStmt = null;
        ResultSet resultSet;
        String sql = null;
        try {
            log.debug("retriving revoked certs after date:" + date + " for tenant :" + tenantId);
            connection = JDBCPersistenceManager.getInstance().getDBConnection();
            sql = "SELECT * FROM CA_REVOKED_CERTIFICATES WHERE TENANT_ID = ? and REVOKED_DATE > ?";
            prepStmt = connection.prepareStatement(sql);

            prepStmt.setInt(1, tenantId);
            prepStmt.setTimestamp(2, new Timestamp(date.getTime()));
            resultSet = prepStmt.executeQuery();
            RevokedCertificate[] revCerts = getRevCertArray(resultSet);
            return revCerts;

        } catch (IdentityException e) {
            String errorMsg = "Error when getting an Identity Persistence Store instance.";
            log.error(errorMsg, e);
            throw new CertAuthException(errorMsg, e);
        } catch (SQLException e) {
            log.error("Error when executing the SQL : " + sql);
            log.error(e.getMessage(), e);
        } finally {
            IdentityDatabaseUtil.closeAllConnections(connection, null, prepStmt);
        }
        return null;
    }

    /**
     * update the revocation reason of a certificate
     *
     * @param tenantId
     * @param serialNo
     * @param reason
     * @throws CertAuthException
     */
    public void updateRevocationReason(int tenantId, String serialNo, int reason) throws CertAuthException {
        Connection connection = null;
        PreparedStatement prepStmt = null;
        ResultSet resultSet;
        String sql = null;
        int result = 0;
        try {
            log.debug("updating revokedCert with serial number :" + serialNo);
            connection = JDBCPersistenceManager.getInstance().getDBConnection();
            sql = "UPDATE CA_REVOKED_CERTIFICATES SET REASON= ? , REVOKED_DATE = ? WHERE SERIAL_NO= ? AND TENANT_ID=?";
            prepStmt = connection.prepareStatement(sql);
            prepStmt.setInt(1, reason);
            prepStmt.setTimestamp(2, new Timestamp(new Date().getTime()));
            prepStmt.setString(3, serialNo);
            prepStmt.setInt(4, tenantId);
            result = prepStmt.executeUpdate();
            connection.commit();
            if (result == 1) {
                log.debug("revoked cert with serial number " + serialNo + " status reason updated to " + reason);
            } else {
                log.debug("error while updating csr with serial number " + serialNo);
            }
        } catch (IdentityException e) {
            String errorMsg = "Error when getting an Identity Persistence Store instance.";
            log.error(errorMsg, e);
            throw new CertAuthException(errorMsg, e);
        } catch (SQLException e) {
            log.error("Error when executing the SQL : " + sql);
            log.error(e.getMessage(), e);
        } finally {
            IdentityDatabaseUtil.closeAllConnections(connection, null, prepStmt);
        }
    }

}

