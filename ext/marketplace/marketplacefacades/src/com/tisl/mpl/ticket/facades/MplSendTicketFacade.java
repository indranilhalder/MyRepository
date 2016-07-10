/**
 *
 */
package com.tisl.mpl.ticket.facades;

import com.tisl.mpl.data.SendTicketRequestData;



/**
 * @author T
 *
 */
public interface MplSendTicketFacade
{
	boolean setTicket(SendTicketRequestData sendTicketRequestData);
}
