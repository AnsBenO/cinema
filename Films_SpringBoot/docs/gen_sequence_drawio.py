#!/usr/bin/env python3
"""Generate UML Sequence Diagrams for Cinema Management System -> sequence_diagram.drawio"""
import os, html, xml.etree.ElementTree as ET

OUT = os.path.join(os.path.dirname(os.path.abspath(__file__)), "sequence_diagram.drawio")
C_ACTOR="#dae8fc"; C_CTRL="#d5e8d4"; C_SVC="#fff2cc"; C_REPO="#ffe6cc"; C_DB="#f8cecc"; C_STROKE="#23445D"
PART_W,PART_H=140,40; SEQ_GAP=200; MSG_Y0=110; MSG_DY=55; EXTRA=40
_c=[0]
def nid(): _c[0]+=1; return "c"+str(_c[0])
def esc(s): return html.escape(str(s), quote=True)
def cx(i): return 80+i*SEQ_GAP

def build_page(name, pid, parts, msgs):
    n=len(parts); h=MSG_Y0+len(msgs)*MSG_DY+EXTRA+80; w=cx(n-1)+PART_W//2+80
    cells=['<mxCell id="0"/>','<mxCell id="1" parent="0"/>']
    for i,(lbl,col) in enumerate(parts):
        px=cx(i); bx=px-PART_W//2; cid=nid()
        cells.append('<mxCell id="'+cid+'" value="'+esc(lbl)+'" style="rounded=1;whiteSpace=wrap;html=1;fillColor='+col+';strokeColor='+C_STROKE+';fontStyle=1;fontSize=11;" vertex="1" parent="1"><mxGeometry x="'+str(bx)+'" y="20" width="'+str(PART_W)+'" height="'+str(PART_H)+'" as="geometry"/></mxCell>')
        ly=20+PART_H; lh=h-ly; lid=nid()
        cells.append('<mxCell id="'+lid+'" value="" style="endArrow=none;dashed=1;strokeDashArray=6 4;strokeColor=#888888;html=1;" edge="1" parent="1"><mxGeometry relative="0" as="geometry"><mxPoint x="'+str(px)+'" y="'+str(ly)+'" as="sourcePoint"/><mxPoint x="'+str(px)+'" y="'+str(ly+lh)+'" as="targetPoint"/></mxGeometry></mxCell>')
    for step,(fi,ti,lbl,dash,note) in enumerate(msgs):
        y=MSG_Y0+step*MSG_DY; fx=cx(fi); tx=cx(ti)
        if note:
            nnd=nid(); cells.append('<mxCell id="'+nnd+'" value="'+esc(lbl)+'" style="text;html=1;strokeColor=none;fillColor=none;align=left;verticalAlign=middle;whiteSpace=wrap;fontStyle=2;fontSize=10;fontColor=#555555;" vertex="1" parent="1"><mxGeometry x="'+str(fx+8)+'" y="'+str(y-10)+'" width="170" height="24" as="geometry"/></mxCell>')
            continue
        s=nid(); t=nid()
        cells.append('<mxCell id="'+s+'" value="" style="point;x=0;y=0;" vertex="1" parent="1"><mxGeometry x="'+str(fx)+'" y="'+str(y)+'" width="1" height="1" as="geometry"/></mxCell>')
        cells.append('<mxCell id="'+t+'" value="" style="point;x=0;y=0;" vertex="1" parent="1"><mxGeometry x="'+str(tx)+'" y="'+str(y)+'" width="1" height="1" as="geometry"/></mxCell>')
        dp="dashed=1;strokeDashArray=5 3;" if dash else ""
        st="endArrow=open;endFill=1;html=1;exitX=0.5;exitY=0.5;exitDx=0;exitDy=0;entryX=0.5;entryY=0.5;entryDx=0;entryDy=0;edgeStyle=orthogonalEdgeStyle;strokeColor=#23445D;fontColor=#333333;fontSize=10;align=center;verticalAlign=bottom;"+dp
        eid=nid(); cells.append('<mxCell id="'+eid+'" value="'+esc(lbl)+'" style="'+st+'" edge="1" source="'+s+'" target="'+t+'" parent="1"><mxGeometry relative="1" as="geometry"/></mxCell>')
    pw=str(max(w,1169)); ph=str(max(h+100,827))
    gxml='<mxGraphModel dx="1422" dy="762" grid="1" gridSize="10" guides="1" tooltips="1" connect="1" arrows="1" fold="1" page="1" pageScale="1" pageWidth="'+pw+'" pageHeight="'+ph+'" math="0" shadow="0"><root>'+''.join(cells)+'</root></mxGraphModel>'
    d=ET.Element("diagram",name=name,id=pid)
    d.append(ET.fromstring(gxml))
    return d

pages=[]

pages.append(build_page("Login Flow","p1",
  [("Browser",C_ACTOR),("AuthController",C_CTRL),("AuthService",C_SVC),("JwtService",C_SVC),("UserDetailsService",C_REPO),("Database",C_DB)],
  [(0,1,"POST /login {username, password}",False,False),
   (1,2,"authenticate(request)",False,False),
   (2,4,"loadUserByUsername(username)",False,False),
   (4,5,"SELECT * FROM users WHERE username=?",False,False),
   (5,4,"UserEntity row",True,False),
   (4,2,"UserDetails",True,False),
   (2,2,"validatePassword(raw, encoded)",False,True),
   (2,3,"generateToken(userDetails)",False,False),
   (3,2,"JWT token string",True,False),
   (2,1,"AuthenticationResponse(token)",True,False),
   (1,0,"200 OK  {token: eyJ...}",True,False)]))

pages.append(build_page("Register User","p2",
  [("Browser",C_ACTOR),("AuthController",C_CTRL),("AuthService",C_SVC),("UserRepository",C_REPO),("Database",C_DB)],
  [(0,1,"POST /register (RegisterUserDto)",False,False),
   (1,2,"register(registerUserDto)",False,False),
   (2,2,"encode password with BCrypt",False,True),
   (2,2,"build UserEntity (role=USER)",False,True),
   (2,3,"save(userEntity)",False,False),
   (3,4,"INSERT INTO users (...) VALUES (...)",False,False),
   (4,3,"UserEntity with generated id",True,False),
   (3,2,"UserEntity",True,False),
   (2,1,"UserDto (registered)",True,False),
   (1,0,"302 Redirect to /login",True,False)]))

pages.append(build_page("JWT Auth Filter","p3",
  [("Client",C_ACTOR),("JwtAuthFilter",C_REPO),("JwtService",C_SVC),("UserDetailsService",C_REPO),("SecurityContext",C_SVC),("Controller",C_CTRL)],
  [(0,1,"HTTP Request  Authorization: Bearer token",False,False),
   (1,1,"extract JWT from header",False,True),
   (1,2,"extractUsername(token)",False,False),
   (2,1,"username",True,False),
   (1,3,"loadUserByUsername(username)",False,False),
   (3,1,"UserDetails",True,False),
   (1,2,"isTokenValid(token, userDetails)",False,False),
   (2,1,"true",True,False),
   (1,4,"setAuthentication(authToken)",False,False),
   (1,5,"filterChain.doFilter(req, res)",False,False),
   (5,0,"HTTP Response",True,False)]))

pages.append(build_page("Add Film","p4",
  [("Admin Browser",C_ACTOR),("FilmController",C_CTRL),("FilmService",C_SVC),("FilmRepository",C_REPO),("Database",C_DB)],
  [(0,1,"POST /films (FilmDto form data)",False,False),
   (1,2,"save(filmDto)",False,False),
   (2,2,"validate and map FilmDto to Film",False,True),
   (2,3,"save(film)",False,False),
   (3,4,"INSERT INTO films (...) VALUES (...)",False,False),
   (4,3,"Film row with generated id",True,False),
   (3,2,"Film entity",True,False),
   (2,1,"FilmDto (persisted)",True,False),
   (1,0,"302 Redirect to /films",True,False)]))

pages.append(build_page("Edit Film","p5",
  [("Admin Browser",C_ACTOR),("FilmController",C_CTRL),("FilmService",C_SVC),("FilmRepository",C_REPO),("Database",C_DB)],
  [(0,1,"GET /films/{id}/edit",False,False),
   (1,2,"findById(id)",False,False),
   (2,3,"findById(id)",False,False),
   (3,4,"SELECT * FROM films WHERE id=?",False,False),
   (4,3,"Film row",True,False),
   (3,2,"Optional Film",True,False),
   (2,1,"FilmDto",True,False),
   (1,0,"200 OK  edit-film.html",True,False),
   (0,1,"POST /films/{id} (updated FilmDto)",False,False),
   (1,2,"update(id, filmDto)",False,False),
   (2,2,"map updated fields",False,True),
   (2,3,"save(film)",False,False),
   (3,4,"UPDATE films SET ... WHERE id=?",False,False),
   (4,3,"updated Film row",True,False),
   (3,2,"Film entity",True,False),
   (2,1,"FilmDto (updated)",True,False),
   (1,0,"302 Redirect to /films",True,False)]))

pages.append(build_page("Rate a Film (API)","p6",
  [("API Client",C_ACTOR),("JwtAuthFilter",C_REPO),("RatingController",C_CTRL),("FilmRatingService",C_SVC),("RatingRepository",C_REPO),("Database",C_DB)],
  [(0,1,"POST /api/films/{id}/ratings  Bearer token + RatingPayload",False,False),
   (1,1,"validate JWT (see JWT Filter page)",False,True),
   (1,2,"forward authenticated request",False,False),
   (2,3,"rateFilm(filmId, payload, user)",False,False),
   (3,4,"findByFilmIdAndUserId(filmId, userId)",False,False),
   (4,5,"SELECT * FROM film_ratings WHERE film_id=? AND user_id=?",False,False),
   (5,4,"Optional FilmRating",True,False),
   (4,3,"Optional FilmRating",True,False),
   (3,3,"create or update FilmRating entity",False,True),
   (3,4,"save(filmRating)",False,False),
   (4,5,"INSERT or UPDATE film_ratings",False,False),
   (5,4,"FilmRating row",True,False),
   (4,3,"FilmRating entity",True,False),
   (3,2,"RatingDto",True,False),
   (2,0,"200 OK  RatingDto",True,False)]))

pages.append(build_page("List Screenings","p7",
  [("Admin Browser",C_ACTOR),("ScreeningController",C_CTRL),("ScreeningService",C_SVC),("ScreeningRepository",C_REPO),("Database",C_DB)],
  [(0,1,"GET /screenings?page=0&size=10",False,False),
   (1,2,"findAll(pageable)",False,False),
   (2,3,"findAllProjectedBy(pageable)",False,False),
   (3,4,"SELECT s.*, f.title, h.name FROM screenings JOIN films JOIN halls",False,False),
   (4,3,"List ScreeningProjection",True,False),
   (3,2,"Page ScreeningProjection",True,False),
   (2,1,"Page ScreeningDto",True,False),
   (1,1,"add to Thymeleaf model",False,True),
   (1,0,"200 OK  list-screenings.html",True,False)]))

pages.append(build_page("Add Screening","p8",
  [("Admin Browser",C_ACTOR),("ScreeningController",C_CTRL),("ScreeningService",C_SVC),("ScreeningRepository",C_REPO),("Database",C_DB)],
  [(0,1,"GET /screenings/add",False,False),
   (1,2,"getFormData()",False,False),
   (2,1,"films list, halls list",True,False),
   (1,0,"200 OK  add-screening.html",True,False),
   (0,1,"POST /screenings (ScreeningDto)",False,False),
   (1,2,"save(screeningDto)",False,False),
   (2,2,"map ScreeningDto to Screening",False,True),
   (2,3,"save(screening)",False,False),
   (3,4,"INSERT INTO screenings (...) VALUES (...)",False,False),
   (4,3,"Screening row with generated id",True,False),
   (3,2,"Screening entity",True,False),
   (2,1,"ScreeningDto (persisted)",True,False),
   (1,0,"302 Redirect to /screenings",True,False)]))

pages.append(build_page("Dashboard","p9",
  [("Admin Browser",C_ACTOR),("DashboardController",C_CTRL),("DashboardService",C_SVC),("Various Repos",C_REPO),("Database",C_DB)],
  [(0,1,"GET /dashboard",False,False),
   (1,2,"getDashboardData()",False,False),
   (2,3,"countFilms()",False,False),
   (3,4,"SELECT COUNT(*) FROM films",False,False),
   (4,3,"film count",True,False),
   (2,3,"countUsers()",False,False),
   (3,4,"SELECT COUNT(*) FROM users",False,False),
   (4,3,"user count",True,False),
   (2,3,"countScreenings()",False,False),
   (3,4,"SELECT COUNT(*) FROM screenings",False,False),
   (4,3,"screening count",True,False),
   (2,3,"getLatestFilms()",False,False),
   (3,4,"SELECT * FROM films ORDER BY id DESC LIMIT 5",False,False),
   (4,3,"latest films",True,False),
   (3,2,"aggregated DashboardDataDto",True,False),
   (2,1,"DashboardDataDto",True,False),
   (1,1,"add data to Thymeleaf model",False,True),
   (1,0,"200 OK  dashboard.html",True,False)]))

root=ET.Element("mxfile",host="app.diagrams.net",version="21.0.0")
for p in pages:
    root.append(p)
tree=ET.ElementTree(root)
ET.indent(tree,space="  ")
tree.write(OUT,encoding="unicode",xml_declaration=True)
print("Written: "+OUT+"  ("+str(len(pages))+" pages)")
