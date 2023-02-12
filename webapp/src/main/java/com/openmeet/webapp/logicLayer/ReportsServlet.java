package com.openmeet.webapp.logicLayer;

import com.openmeet.shared.data.meeter.Meeter;
import com.openmeet.shared.data.report.Report;
import com.openmeet.shared.utils.MultiMapList;
import com.openmeet.shared.utils.QueryJoinExecutor;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Servlet that handles the reports functionality.
 *
 * @author Angelo Nazzaro
 */
public class ReportsServlet extends HttpServlet {

    /**
     * Gets the reports and displays them.
     *
     * @param req  The request object.
     * @param resp The response object.
     * @throws ServletException If an error occurs.
     * @throws IOException      If an error occurs.
     *
     * @author Angelo Nazzaro
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        req.setAttribute("view", "reports");
        req.setAttribute("title", "Reports");
        req.setAttribute("heading", "Reports");

        // Get Data
        QueryJoinExecutor qjx = new QueryJoinExecutor((DataSource) getServletContext().getAttribute("DataSource"));
        MultiMapList<String, String> data = new MultiMapList<>();

        try {
            data = qjx.doRetrivedByJoin(
                    String.format(
                            "SELECT %s.*, CONCAT(%s, ' ', %s) AS meeterfullName, %s " +
                                    "FROM %s JOIN %s ON %s = %s " +
                                    "WHERE %s = FALSE",
                            Report.REPORT, Meeter.MEETER_MEETER_NAME, Meeter.MEETER_MEETER_SURNAME, Meeter.MEETER_EMAIL,
                            Report.REPORT, Meeter.MEETER, Meeter.MEETER_ID, Report.REPORT_MEETER_REPORTED,
                            Report.REPORT_IS_ARCHIVED));

        } catch (SQLException e) {
            resp.sendError(500, "Internal Server error");
        }

        req.setAttribute("data", data);

        req.getRequestDispatcher("WEB-INF/index.jsp").forward(req, resp);
    }
}
