/*
 * Copyright (c) 2018, Xyneex Technologies. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * You are not meant to edit or modify this source code unless you are
 * authorized to do so.
 *
 * Please contact Xyneex Technologies, #1 Orok Orok Street, Calabar, Nigeria.
 * or visit www.xyneex.com if you need additional information or have any
 * questions.
 */
package com.valdbms.members;

import com.valdbms.banks.BankDAO;
import com.valdbms.roles.RolesDAO;
import com.valdbms.wards.LGA_DAO;
import com.valdbms.wards.Ward;
import com.valdbms.wards.WardDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;

/**
 *
 * @author Jevison7x
 */
public class NewMemberFormServlet extends HttpServlet
{
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        try
        {
            if(request.getParameter("getWards") != null)
            {
                String lga = request.getParameter("lga");
                String state = request.getParameter("state");
                List<Ward> wards = WardDAO.getWards(state, lga);
                JSONArray jSONArray = new JSONArray(wards);
                try(PrintWriter out = response.getWriter())
                {
                    response.setContentType("application/json");
                    out.print(jSONArray);
                }
            }
            else if(request.getParameter("getLGAs") != null)
            {
                String state = request.getParameter("state");
                List<String> lgaList = LGA_DAO.getDistinctLGAs(state);
                JSONArray jSONArray = new JSONArray(lgaList);
                try(PrintWriter out = response.getWriter())
                {
                    response.setContentType("application/json");
                    out.print(jSONArray);
                }
            }
            else
            {
                List<String> roleList = RolesDAO.getDistinctRoles();
                List<String> banksList = BankDAO.getDistinctBanks();
                request.setAttribute("roleList", roleList);
                request.setAttribute("banksList", banksList);
                request.getRequestDispatcher("new-member-form").forward(request, response);
            }
        }
        catch(Exception xcp)
        {
            throw new RuntimeException(xcp);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo()
    {
        return "Short description";
    }// </editor-fold>

}
