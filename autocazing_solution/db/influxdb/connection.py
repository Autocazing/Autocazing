from influxdb import InfluxDBClient

class InfluxDBConnection:
    def __init__(self, token: str):
        self.token = token
        self.client = None

    def connect(self):
        """Instantiate a connection to the InfluxDB."""
        self.client = InfluxDBClient(username=None, password=None, headers={"Authorization": self.token})
        print("Use authorization token: " + self.token)
        version = self.client.ping()
        print("Successfully connected to InfluxDB: " + version)

    def get_client(self) -> InfluxDBClient:
        """Return the InfluxDB client instance."""
        return self.client