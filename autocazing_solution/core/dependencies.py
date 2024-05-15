from db.mysql.session import mysqlSession

# Dependency injection for database session
def get_db():
    db = mysqlSession.get_db()
    try:
        yield db
    finally:
        db.close()