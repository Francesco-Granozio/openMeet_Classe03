package com.openmeet.webservice.services;

import com.openmeet.shared.data.image.Image;
import com.openmeet.shared.data.image.ImageDAO;
import com.openmeet.shared.data.storage.DAO;
import com.openmeet.shared.helpers.ResponseHelper;
import com.openmeet.webservice.exceptions.InvalidParameterException;
import com.openmeet.webservice.proxies.ImageProxyDAO;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servlet that handles all invocations for the Image object.
 *
 * @author Roberto Della Rocca
 */
public class ImageService extends HttpServlet {

    private static final Logger logger = Logger.getLogger(ImageService.class.getName());

    /**
     * On POST request, the method gets the name of operation from "operation" paramater and then invoke the following proxy method.
     *
     * @param request  an {@link HttpServletRequest} object that contains the request the client has made of the servlet.
     * @param response an {@link HttpServletResponse} object that contains the response the servlet sends to the client
     * @throws IOException
     * 
     * @author Roberto Della Rocca
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String operation = request.getParameter("operation");
        PrintWriter out = response.getWriter();

        //check if all the parameters are present
        if (!ResponseHelper.checkStringFields(operation)) {
            ResponseHelper.sendGenericError(out);
            logger.log(Level.SEVERE, "ImageService:doPost() - Error: missing parameters - operation");
            return;
        }

        ImageDAO imageDAO = new ImageDAO((DataSource) getServletContext().getAttribute("DataSource"));
        ImageProxyDAO imageProxyDAO = new ImageProxyDAO(imageDAO, request, out);

        switch (operation) {
            case DAO.DO_RETRIEVE_BY_CONDITION: {
                try {
                    imageProxyDAO.doRetrieveByCondition(null);
                } catch (SQLException | InvalidParameterException e) {
                    ResponseHelper.sendGenericError(out);
                    logger.log(Level.SEVERE, "ImageService:doPost() - Error: " + e.getMessage());
                    return;
                }
                break;
            }
            case DAO.DO_RETRIEVE_BY_CONDITION_LIMIT: {
                try {
                    imageProxyDAO.doRetrieveByCondition(null, 0);
                } catch (SQLException | InvalidParameterException e) {
                    ResponseHelper.sendGenericError(out);
                    logger.log(Level.SEVERE, "ImageService:doPost() - Error: " + e.getMessage());
                    return;
                }
                break;
            }
            case DAO.DO_RETRIEVE_BY_CONDITION_LIMIT_OFFSET: {
                try {
                    imageProxyDAO.doRetrieveByCondition(null, 0, 0);
                } catch (SQLException | InvalidParameterException e) {
                    ResponseHelper.sendGenericError(out);
                    logger.log(Level.SEVERE, "ImageService:doPost() - Error: " + e.getMessage());
                    return;
                }
                break;
            }
            case DAO.DO_RETRIEVE_BY_KEY: {
                try {
                    imageProxyDAO.doRetrieveByKey(null);
                } catch (SQLException | InvalidParameterException e) {
                    ResponseHelper.sendGenericError(out);
                    logger.log(Level.SEVERE, "ImageService:doPost() - Error: " + e.getMessage());
                    return;
                }
                break;
            }
            case DAO.DO_RETRIEVE_ALL: {
                try {
                    imageProxyDAO.doRetrieveAll();
                } catch (SQLException e) {
                    ResponseHelper.sendGenericError(out);
                    logger.log(Level.SEVERE, "ImageService:doPost() - Error: " + e.getMessage());
                    return;
                }
                break;
            }
            case DAO.DO_RETRIEVE_ALL_LIMIT: {
                try {
                    imageProxyDAO.doRetrieveAll(0);
                } catch (SQLException | InvalidParameterException e) {
                    ResponseHelper.sendGenericError(out);
                    logger.log(Level.SEVERE, "ImageService:doPost() - Error: " + e.getMessage());
                    return;
                }
                break;
            }
            case DAO.DO_RETRIEVE_ALL_LIMIT_OFFSET: {
                try {
                    imageProxyDAO.doRetrieveAll(0, 0);
                } catch (SQLException | InvalidParameterException e) {
                    ResponseHelper.sendGenericError(out);
                    logger.log(Level.SEVERE, "ImageService:doPost() - Error: " + e.getMessage());
                    return;
                }
                break;
            }
            case DAO.DO_SAVE: {
                try {
                    imageProxyDAO.doSave(new Image());
                } catch (SQLException | InvalidParameterException e) {
                    ResponseHelper.sendGenericError(out);
                    logger.log(Level.SEVERE, "ImageService:doPost() - Error: " + e.getMessage());
                    return;
                }
                break;
            }
            case DAO.DO_SAVE_PARTIAL:
                E:
                {
                    try {
                        imageProxyDAO.doSave(new HashMap<>());
                    } catch (SQLException | InvalidParameterException e) {
                        ResponseHelper.sendGenericError(out);
                        logger.log(Level.SEVERE, "ImageService:doPost() - Error: " + e.getMessage());
                        return;
                    }
                    break;
                }
            case DAO.DO_UPDATE: {
                try {
                    imageProxyDAO.doUpdate(null, null);
                } catch (SQLException | InvalidParameterException e) {
                    ResponseHelper.sendGenericError(out);
                    logger.log(Level.SEVERE, "ImageService:doPost() - Error: " + e.getMessage());
                    return;
                }
                break;
            }
            case DAO.DO_SAVE_OR_UPDATE: {
                try {
                    imageProxyDAO.doSaveOrUpdate(null);
                } catch (SQLException | InvalidParameterException e) {
                    ResponseHelper.sendGenericError(out);
                    logger.log(Level.SEVERE, "ImageService:doPost() - Error: " + e.getMessage());
                    return;
                }
                break;
            }
            case DAO.DO_DELETE: {
                try {
                    imageProxyDAO.doDelete(null);
                } catch (SQLException | InvalidParameterException e) {
                    ResponseHelper.sendGenericError(out);
                    logger.log(Level.SEVERE, "ImageService:doPost() - Error: " + e.getMessage());
                    return;
                }
                break;
            }
            default:
                ResponseHelper.sendGenericError(out);
                logger.log(Level.SEVERE, "ImageService:doPost() - Error: unknown operation");
                break;
        }
    }

}
