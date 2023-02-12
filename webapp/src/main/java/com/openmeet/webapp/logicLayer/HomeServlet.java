package com.openmeet.webapp.logicLayer;

import com.google.gson.Gson;
import com.openmeet.shared.data.report.Report;
import com.openmeet.shared.data.report.ReportDAO;
import com.openmeet.shared.helpers.JSONResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * This class is used to handle the home page.
 *
 * @author Angelo Nazzaro
 */
public class HomeServlet extends HttpServlet {
    /**
     * Displays the dashboard view to the user if logged.
     *
     * @param req  The request object.
     * @param resp The response object.
     * @throws ServletException If an error occurs.
     * @throws IOException      If an error occurs.
     *
     * @author Angelo Nazzaro
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("view", "dashboard");
        req.setAttribute("title", "Dashboard");

        DataSource dataSource = (DataSource) getServletContext().getAttribute("DataSource");
        ReportDAO reportDAO = new ReportDAO(dataSource);
        Gson gson = new Gson();

        try {
            // Get Report Ratio data (Archived vs Unarchived)
            List<Report> unarchivedReports = reportDAO.doRetrieveByCondition(String.format("%s = FALSE", Report.REPORT_IS_ARCHIVED));
            List<Report> archivedReports = reportDAO.doRetrieveByCondition(String.format("%s = TRUE", Report.REPORT_IS_ARCHIVED));

            // Calculate Percentages
            int totalReports = unarchivedReports.size() + archivedReports.size();
            float unarchivedReportsPcg = ((float) unarchivedReports.size() / totalReports) * 100;
            float archivedReportsPcg = ((float) archivedReports.size() / totalReports) * 100;

            JSONResponse reportsData = new JSONResponse();
            reportsData.addPair("unarchivedReportsPcg", String.valueOf(unarchivedReportsPcg));
            reportsData.addPair("archivedReportsPcg", String.valueOf(archivedReportsPcg));

            req.setAttribute("reportsData", gson.toJson(reportsData.getResponse()));

        } catch (SQLException e) {
            resp.sendError(500, "Internal Server Error");
        }

        req.getRequestDispatcher("WEB-INF/index.jsp").forward(req, resp);
    }
}
