<?php session_start(); ?>
<?php ob_start(); ?>
<?php include("includes/header.php"); ?>
<?php include("includes/functions.php"); ?>
<?php
    if(!isset($_SESSION['id']))
{
	header("Location: login.php");
	exit;
}
?>
	<title>Trekroute Project</title>
<link rel="stylesheet" type="text/css" href="stylesheets/style.css">
</head>

<body>
<div id="wrap">
<div class="hearder-nav">
	<div class="logo">
		TrekRoute Logo
		</div>
<p class="clear-fix">&nbsp;</p>
</div> <!-- End of header-nav -->

<div id="menu">
<ul>
		<li><a href="index.php">Home</a></li>
	<?php
	$route_set = confirm_query("SELECT * FROM route LIMIT 5");
	while($route = mysql_fetch_array($route_set)){
		if ($temp != $route["type"])
		{
			$url = "route-type.php?";
			$temp = urlencode($route["type"]);
			$url .= "type=".$temp;
	 		echo "<li><a href=\"$url\">".ucfirst($route["type"])."</a></li>";
	    } 
		$temp = $route["type"];
		} 
	?>
	</ul>
</div> <!-- End of menu -->

<div id="content">
<div id="sidebar"><p>	
<ul class="sitebar-nav">

</ul>
</div> <!-- End of sitebar-->
<div id="main-content"> 
   <h2>Staff Menu</h2>
<p>Welcome <?php echo ucfirst($_SESSION['name']); ?></p>
<a href="new_route.php">Add new route</a>
<a href=""></a>
<a href="logout.php">Logout</a>
<a>
	</div> <!-- End of content-->
<?php ob_end_flush(); ?>
<?php include("includes/footer.php"); ?>