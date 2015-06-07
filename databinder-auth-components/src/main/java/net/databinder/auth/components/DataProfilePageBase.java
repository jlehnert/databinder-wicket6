/*
 * Databinder: a simple bridge from Wicket to Hibernate
 * Copyright (C) 2006  Nathan Hamblen nathan@technically.us
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package net.databinder.auth.components;

import net.databinder.auth.components.DataSignInPageBase.ReturnPage;
import net.databinder.components.TagField;

import org.apache.wicket.Component;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.resource.CssResourceReference;

/**
 * Display profile editing page for logged in user. 
 * Replaceable String resources: <pre>
 * data.auth.update
 * data.auth.title.update
 */
public abstract class DataProfilePageBase extends WebPage {
	public DataProfilePageBase(ReturnPage returnPage) {
		add(new Label("title", new ResourceModel("data.auth.title.update", "Update Account")));
		add(new Label("text", new ResourceModel("data.auth.update", "Update Account")));
		add(profileSocket("profileSocket", returnPage));
	}
	/** @return component to appear as the body of this page. */
	protected abstract Component profileSocket(String id, ReturnPage returnPage);
	
	@Override
	public void renderHead(IHeaderResponse response) {
		response.render(CssHeaderItem.forReference(new CssResourceReference(TagField.class, "DataStyleSheet.css")));
	}
}
