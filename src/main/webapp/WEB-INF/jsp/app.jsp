<%@ include file="/src/main/webapp/WEB-INF/jsp/header.jsp"%>

<c:url var="home" value="/" />
<c:url var="app" value="/app.js" />

<div id="myApp">
    <div class="container">
        <h1>My application</h1>
        <p>Hello.</p>
    </div>
</div>
<script src="${app}"></script>

<%@ include file="/src/main/webapp/WEB-INF/jsp/footer.jsp"%>