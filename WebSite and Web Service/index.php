<?php include("includes/header.php") ?>
<?php include("includes/functions.php")?>
	<title>Trek Route Project</title>
<link rel="stylesheet" type="text/css" href="stylesheets/style.css">
</head>
<body>
<div id="wrap">
<div class="hearder-nav">
	<div class="logo">
		TrekRoute Logo
		</div>
<div class="search">
	<form  action="process.php">
    <input class="text" size="35" type="text" name="search" >&nbsp;&nbsp;
  	<input class="submit" type="submit" value="Search">
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
<h2>Destinations to Visit</h2>
<ul class="sitebar-nav">
<?php
$route_set = confirm_query("SELECT * FROM route");
while($route = mysql_fetch_array($route_set)){
	$upper_route_type = ucfirst($route["type"]);
	$upper_route_name = ucfirst($route["name"]);
	echo "<li><a href=\"edit.php?id={$route["id"]}\">{$upper_route_name} {$upper_route_type}</a></li>";
	}
?>
</ul>
<p></b><a href="new_route.php">+ Add a new route</a></b></p>
</div> <!-- End of sitebar-->
<div id="main-content"> <p>This is main page content. But, I got nothing interesting for you coz, you are nothing. This is main page content. But, I got nothing interesting for you coz, you are nothing. This is main page content. But, I got nothing intasdf ajfh kjadsf eresting for you coz, you are nothing. This is main page content. But, I got nothing interesting for you coz, you are nothing. This is main page content. But, I got nothing interesting for you coz, you are nothing. This is main page content. But, I got nothinadf adfs adsf g interesting for yousdfaasd asdfsd  coz, you are nothing. This adsfasdis main page content. But, I got nothing adsfinteresting for you coz, you are nothing. This is main page content. But, I got nothing interesting for you coz, you are nothing. This is main page content. But, I got nothingasf interesting for you coz, you are nothing. This is main page content. But, I got nothing interesafdsting for you coz, you are nothing. Goood bye </p> <p>This is main page content. But, I got nothing interesting for you coz, you are nothing. This is main page contensdft. But, I got nothing iasdfnteradfsesting for you coz, you are nothing. This is main page content. But, I got nothing interesting for you coz, you are nothing. This is main page content. But, I got nothing interesting for you coz, you are nothing. This is main page content. But, I got nothing interesting for you coz, you are nosdfthing. This is main asdfapage content. Buaft, I got nothing interesting for you coz, you are nothing. This is main page content. But, I got nothing inasateresting for you coz, you are nothing. This is main page content. But, I got nothing interesting for you coz, you are nothing. This is main page content. But, I got nothing interesting for you coz, you are nothing. This is main page contentgad. But, I got nothing interafdesting for you coz, you are nothing. </p><p>This design is awesome. #Hala me...lol</p>
	</div> <!-- End of main content -->
	<p class="clear-fix">&nbsp;</p>
	</div> <!-- End of content-->
<?php include("includes/footer.php")?>