#!/usr/bin/env python3
"""Generate UML Use Case Diagram for Cinema Management System in draw.io format."""
import html
import os

OUT = os.path.join(os.path.dirname(__file__), "use_case_diagram.drawio")


def esc(s: str) -> str:
    return html.escape(s, quote=True).replace("\n", "&#xa;")


def cell(cid, val, style, x, y, w, h):
    return (
        f'        <mxCell id="{cid}" value="{esc(val)}" style="{style}" '
        f'vertex="1" parent="1">'
        f'<mxGeometry x="{x}" y="{y}" width="{w}" height="{h}" as="geometry"/>'
        f"</mxCell>\n"
    )


def actor(cid, val, x, y, fill, stroke):
    s = (
        f"shape=mxgraph.uml.actor2;whiteSpace=wrap;html=1;fontSize=12;fontStyle=1;"
        f"fillColor={fill};strokeColor={stroke};"
    )
    return (
        f'        <mxCell id="{cid}" value="{esc(val)}" style="{s}" '
        f'vertex="1" parent="1">'
        f'<mxGeometry x="{x}" y="{y}" width="60" height="90" as="geometry"/>'
        f"</mxCell>\n"
    )


def uc(cid, val, x, y, fill, stroke):
    s = f"ellipse;whiteSpace=wrap;html=1;fontSize=11;fillColor={fill};strokeColor={stroke};"
    return cell(cid, val, s, x, y, 175, 55)


def box(cid, val, x, y, w, h):
    s = (
        "rounded=1;whiteSpace=wrap;html=1;fillColor=#f5f5f5;strokeColor=#666666;"
        "strokeWidth=2;fontSize=13;fontStyle=1;verticalAlign=top;spacingTop=8;"
        "align=center;fontColor=#333333;"
    )
    return cell(cid, val, s, x, y, w, h)


def edge(eid, src, tgt, lbl="", dashed=False):
    dash = "dashed=1;" if dashed else ""
    s = f"edgeStyle=orthogonalEdgeStyle;{dash}endArrow=open;endFill=0;html=1;"
    return (
        f'        <mxCell id="{eid}" value="{esc(lbl)}" style="{s}" '
        f'edge="1" source="{src}" target="{tgt}" parent="1">'
        f'<mxGeometry relative="1" as="geometry"/>'
        f"</mxCell>\n"
    )


def note(cid, val, x, y, w, h):
    s = (
        "rounded=1;whiteSpace=wrap;html=1;fillColor=#fff2cc;strokeColor=#d6b656;"
        "fontSize=11;align=center;"
    )
    return cell(cid, val, s, x, y, w, h)


# ── Colour palette ────────────────────────────────────────────────────────────
BF, BS = "#dae8fc", "#6c8ebf"   # blue   – anonymous / auth
GF, GS = "#d5e8d4", "#82b366"   # green  – admin
OF, OS = "#ffe6cc", "#d6b656"   # orange – api client
PF, PS = "#e1d5e7", "#9673a6"   # purple – rating

parts = []

# ── SYSTEM BOUNDARY ───────────────────────────────────────────────────────────
parts.append(cell(
    "sys", "Cinema Management System",
    "rounded=0;whiteSpace=wrap;html=1;fillColor=none;strokeColor=#333333;"
    "strokeWidth=3;fontSize=20;fontStyle=1;verticalAlign=top;spacingTop=14;align=center;",
    300, 40, 3820, 1640,
))

# ── ACTORS ────────────────────────────────────────────────────────────────────
parts.append(actor("a_anon",  "Anonymous User",  80,  200, BF, BS))
parts.append(actor("a_admin", "Admin",           80,  700, GF, GS))
parts.append(actor("a_api",   "API Client",    4200,  700, OF, OS))

# ── SEC 1: AUTHENTICATION ─────────────────────────────────────────────────────
parts.append(box("b_auth", "Authentication", 360, 100, 550, 290))
for cid, val, y in [
    ("uc_reg",    "Register",  150),
    ("uc_login",  "Login",     230),
    ("uc_logout", "Logout",    310),
]:
    parts.append(uc(cid, val, 385, y, BF, BS))
    parts.append(edge(f"e_anon_{cid}", "a_anon", cid))
parts.append(uc("uc_jwt", "JWT Token Authentication", 650, 230, BF, BS))
parts.append(edge("e_login_jwt", "uc_login", "uc_jwt", "include", dashed=True))

# ── SEC 2: DASHBOARD ──────────────────────────────────────────────────────────
parts.append(box("b_dash", "Dashboard", 980, 100, 240, 120))
parts.append(uc("uc_dash", "View Dashboard", 990, 140, GF, GS))
parts.append(edge("e_adm_dash", "a_admin", "uc_dash"))

# ── SEC 3: FILM MANAGEMENT ────────────────────────────────────────────────────
parts.append(box("b_film", "Film Management", 360, 450, 700, 380))
for i, (cid, val) in enumerate([
    ("uc_film_list",  "List Films"),
    ("uc_film_add",   "Add Film"),
    ("uc_film_edit",  "Edit Film"),
    ("uc_film_del",   "Delete Film"),
]):
    parts.append(uc(cid, val, 385, 490 + i * 75, GF, GS))
    parts.append(edge(f"e_adm_{cid}", "a_admin", cid))
parts.append(uc("uc_film_cover", "Upload Film Cover", 650, 565, GF, GS))
parts.append(edge("e_adm_film_cover", "a_admin", "uc_film_cover"))
parts.append(edge("e_add_cover", "uc_film_add", "uc_film_cover", "extend", dashed=True))

# ── SEC 4: GENRE MANAGEMENT ───────────────────────────────────────────────────
parts.append(box("b_genre", "Genre Management", 1120, 450, 260, 310))
for i, (cid, val) in enumerate([
    ("uc_genre_list", "List Genres"),
    ("uc_genre_add",  "Add Genre"),
    ("uc_genre_edit", "Edit Genre"),
    ("uc_genre_del",  "Delete Genre"),
]):
    parts.append(uc(cid, val, 1140, 490 + i * 68, GF, GS))
    parts.append(edge(f"e_adm_{cid}", "a_admin", cid))

# ── SEC 5: HALL MANAGEMENT ────────────────────────────────────────────────────
parts.append(box("b_hall", "Hall Management", 1440, 450, 260, 310))
for i, (cid, val) in enumerate([
    ("uc_hall_list", "List Halls"),
    ("uc_hall_add",  "Add Hall"),
    ("uc_hall_edit", "Edit Hall"),
    ("uc_hall_del",  "Delete Hall"),
]):
    parts.append(uc(cid, val, 1460, 490 + i * 68, GF, GS))
    parts.append(edge(f"e_adm_{cid}", "a_admin", cid))

# ── SEC 6: SCREENING MANAGEMENT ───────────────────────────────────────────────
parts.append(box("b_screen", "Screening Management", 1760, 450, 280, 310))
for i, (cid, val) in enumerate([
    ("uc_screen_list", "List Screenings"),
    ("uc_screen_add",  "Add Screening"),
    ("uc_screen_edit", "Edit Screening"),
    ("uc_screen_del",  "Delete Screening"),
]):
    parts.append(uc(cid, val, 1780, 490 + i * 68, GF, GS))
    parts.append(edge(f"e_adm_{cid}", "a_admin", cid))

# ── SEC 7: PERSON MANAGEMENT ──────────────────────────────────────────────────
parts.append(box("b_person", "Person Management", 2100, 450, 260, 310))
for i, (cid, val) in enumerate([
    ("uc_person_list", "List Persons"),
    ("uc_person_add",  "Add Person"),
    ("uc_person_edit", "Edit Person"),
    ("uc_person_del",  "Delete Person"),
]):
    parts.append(uc(cid, val, 2120, 490 + i * 68, GF, GS))
    parts.append(edge(f"e_adm_{cid}", "a_admin", cid))

# ── SEC 8: NATIONALITY MANAGEMENT ─────────────────────────────────────────────
parts.append(box("b_nat", "Nationality Management", 2420, 450, 280, 310))
for i, (cid, val) in enumerate([
    ("uc_nat_list", "List Nationalities"),
    ("uc_nat_add",  "Add Nationality"),
    ("uc_nat_edit", "Edit Nationality"),
    ("uc_nat_del",  "Delete Nationality"),
]):
    parts.append(uc(cid, val, 2440, 490 + i * 68, GF, GS))
    parts.append(edge(f"e_adm_{cid}", "a_admin", cid))

# ── SEC 9: MEDIA MANAGEMENT ───────────────────────────────────────────────────
parts.append(box("b_media", "Media Management", 2760, 450, 260, 240))
for i, (cid, val) in enumerate([
    ("uc_media_add",  "Add Film Media"),
    ("uc_media_list", "List Film Media"),
    ("uc_media_del",  "Delete Film Media"),
]):
    parts.append(uc(cid, val, 2780, 490 + i * 68, GF, GS))
    parts.append(edge(f"e_adm_{cid}", "a_admin", cid))

# ── SEC 10: USER MANAGEMENT ───────────────────────────────────────────────────
parts.append(box("b_user", "User Management", 3080, 450, 260, 310))
for i, (cid, val) in enumerate([
    ("uc_user_list", "List Users"),
    ("uc_user_add",  "Add User"),
    ("uc_user_edit", "Edit User"),
    ("uc_user_del",  "Delete User"),
]):
    parts.append(uc(cid, val, 3100, 490 + i * 68, GF, GS))
    parts.append(edge(f"e_adm_{cid}", "a_admin", cid))

# ── SEC 11: FILM RATING (Web UI) ──────────────────────────────────────────────
parts.append(box("b_rate_ui", "Film Rating (Web UI)", 3400, 450, 280, 240))
for i, (cid, val) in enumerate([
    ("uc_rate_list", "List Film Ratings"),
    ("uc_rate_add",  "Rate a Film"),
    ("uc_rate_del",  "Delete Rating"),
]):
    parts.append(uc(cid, val, 3420, 490 + i * 68, PF, PS))
    parts.append(edge(f"e_adm_{cid}", "a_admin", cid))

# ── SEC 12: REST API ENDPOINTS ────────────────────────────────────────────────
parts.append(box("b_api", "REST API Endpoints", 3380, 830, 490, 660))
for i, (cid, val) in enumerate([
    ("uc_api_login",       "POST /api/auth/login"),
    ("uc_api_register",    "POST /api/auth/register"),
    ("uc_api_films_get",   "GET  /api/films"),
    ("uc_api_films_post",  "POST /api/films"),
    ("uc_api_rating_get",  "GET  /api/films/{id}/ratings"),
    ("uc_api_rating_post", "POST /api/films/{id}/ratings"),
    ("uc_api_media_get",   "GET  /api/media/{filmId}"),
    ("uc_api_media_post",  "POST /api/media/{filmId}"),
]):
    parts.append(uc(cid, val, 3400, 865 + i * 72, OF, OS))
    parts.append(edge(f"e_api_{cid}", "a_api", cid))

# ── SEC 13: SECURITY NOTES ────────────────────────────────────────────────────
parts.append(box("b_sec", "Security Constraints", 360, 1720, 3520, 200))
for cid, val, x in [
    ("n1", "Login required for all Admin use cases",      420),
    ("n2", "JWT token required for REST API",             700),
    ("n3", "ROLE_ADMIN required for management ops",      980),
    ("n4", "Anonymous: register and login only",         1260),
    ("n5", "Passwords hashed with BCrypt",               1540),
    ("n6", "Stateless sessions (JWT)",                   1820),
]:
    parts.append(note(cid, val, x, 1760, 220, 80))

# ── ASSEMBLE XML ──────────────────────────────────────────────────────────────
header = (
    '<?xml version="1.0" encoding="UTF-8"?>\n'
    '<mxGraphModel dx="2000" dy="1200" grid="1" gridSize="10" guides="1" '
    'tooltips="1" connect="1" arrows="1" fold="1" page="1" pageScale="1" '
    'pageWidth="4681" pageHeight="3300" math="0" shadow="0">\n'
    '    <root>\n'
    '        <mxCell id="0"/>\n'
    '        <mxCell id="1" parent="0"/>\n'
)
footer = '    </root>\n</mxGraphModel>\n'

with open(OUT, "w", encoding="utf-8") as f:
    f.write(header)
    f.writelines(parts)
    f.write(footer)

print(f"Generated: {OUT}")
