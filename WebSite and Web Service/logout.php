<?php
ob_start();
	session():
	$_SESSION = array();
	if (!$isset($_COOKIE[session_name()]))
	{
		setcookie(sesion_name(),'',time()-348934,'/');
	}
	session_destroy();
	header("login.php?logout=1");
	exit;
ob_flush_end();
?>