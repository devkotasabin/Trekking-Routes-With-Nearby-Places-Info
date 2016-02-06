<?php ob_start(); ?>
<?php include("includes/header.php") ?>
<?php include("includes/functions.php")?>
<?php
$id = $_GET['id'];
$query = "DELETE FROM route WHERE id={$id} LIMIT 1";
if(mysql_query($query))
{
	header("Location: index.php");
	exit;
}
else
{
	echo "Error in deletion";
}
ob_flush_end();
?>
<?php mysql_close() ?>