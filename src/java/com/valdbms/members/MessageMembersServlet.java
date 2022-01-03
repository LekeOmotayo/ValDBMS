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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author Jevison7x
 */
public class MessageMembersServlet extends HttpServlet
{
    private static final String SENDER_ID = "Val Ozigbo";
    private static final String API_TOKEN = "ZoiNwvO08ydKuvVgcqjCgl8cxcXURhdAPvnBjrScpGwvqIJ1zkS4Sks8Nrmk";
    private static final String SMS_URL = "https://www.bulksmsnigeria.com/api/v1/sms/create";

    //MultiTexter URL, Email and Password
    private static final String MULTI_TEXTER_URL = "https://app.multitexter.com/v2/app/sms";
    private static final String EMAIL = "faithannie15@gmail.com";
    private static final String PASSWORD = "christ123@";
    private static final long serialVersionUID = 1L;

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
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try
        {
            HttpSession session = request.getSession(false);
            Set<String> phonenumbers = (Set<String>)session.getAttribute("selectedPhoneNumbers");
            Map<String, String> filterParameters = (Map<String, String>)session.getAttribute("filterParameters");
            if(phonenumbers == null)
                if(filterParameters != null)
                {
                    String role = filterParameters.get("role");
                    String state = filterParameters.get("state");
                    String lga = filterParameters.get("lga");
                    String ward = filterParameters.get("ward");
                    String bank = filterParameters.get("bank");
                    List<Member> filteredMembers = MembersDAO.filteredMembers(role, state, lga, ward, bank);
                    phonenumbers = new TreeSet<>();
                    for(Member member : filteredMembers)
                        phonenumbers.add(member.getMobileNo());
                }
            String originalMessage = request.getParameter("message");
            if(phonenumbers != null)
                for(String phoneNumber : phonenumbers)
                {
                    String smsMessage = originalMessage;
                    if(originalMessage.contains("${firstName}") || originalMessage.contains("${lastName}"))
                    {
                        Member member = MembersDAO.getMember(phoneNumber);
                        String firstName = member.getFirstName();
                        String lastName = member.getLastName();
                        smsMessage = smsMessage.replace("${firstName}", firstName);
                        smsMessage = smsMessage.replace("${lastName}", lastName);
                    }
                    //String smsResponse = MembersDAO.sendSmartSMSSolutions(smsMessage, SENDER_ID, phoneNumber);
                    //String smsResponse = MembersDAO.sendBulkSMSNigeria(SENDER_ID, phoneNumber, smsMessage, SMS_URL, API_TOKEN);
                    //System.out.println("SMS Response: " + smsResponse);
                    //out.print(phoneNumber + ", ");
                    MembersDAO.sendMultiTexter(EMAIL, PASSWORD, smsMessage, MULTI_TEXTER_URL, SENDER_ID, phoneNumber);
                }
            else
                out.print("empty phone numbers");
        }
        catch(Exception xcp)
        {
            xcp.printStackTrace(out);
            xcp.printStackTrace(System.err);
        }
        finally
        {
            out.close();
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
