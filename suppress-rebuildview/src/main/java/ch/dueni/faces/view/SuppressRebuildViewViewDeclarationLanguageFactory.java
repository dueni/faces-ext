/**
 * Copyright 2009 by dueni.ch
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ch.dueni.faces.view;

import javax.faces.view.ViewDeclarationLanguage;
import javax.faces.view.ViewDeclarationLanguageFactory;

/**
 * VDL factory to hook in {@link SuppressRebuildViewViewDeclarationLanguageWrapper}.
 * 
 * @author hampidu@gmail.com
 *
 */
public class SuppressRebuildViewViewDeclarationLanguageFactory extends
		ViewDeclarationLanguageFactory {

	private ViewDeclarationLanguageFactory delegate;

	public SuppressRebuildViewViewDeclarationLanguageFactory(
			ViewDeclarationLanguageFactory delegate) {
		this.delegate = delegate;
	}

	@Override
	public ViewDeclarationLanguage getViewDeclarationLanguage(String viewId) {
		return new SuppressRebuildViewViewDeclarationLanguageWrapper(
				delegate.getViewDeclarationLanguage(viewId));
	}

}
