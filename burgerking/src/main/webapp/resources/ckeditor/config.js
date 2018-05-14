/**
 * @license Copyright (c) 2003-2015, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.md or http://ckeditor.com/license
 */

CKEDITOR.editorConfig = function( config ) {
	// Define changes to default configuration here. For example:
	// config.language = 'fr';
	// config.uiColor = '#AADC6E';
	 config.image_previewText=' '; //预览区域显示内容
	 config.filebrowserImageUploadUrl= "/burgerking/product/uploadImg"; //要上传的action或servlet
	 config.baseHref = 'http://192.168.10.155:8080/burgerking/';
	 config.width = 1000;
	 config.height = 500;
};
