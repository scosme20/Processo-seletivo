from flask import Flask, request, jsonify
from flask_cors import CORS
import csv
import difflib

app = Flask(__name__)
CORS(app)

data = []
with open('Relatorio_cadop_12_clean.csv', encoding='utf-8') as csvfile:
    reader = csv.DictReader(csvfile, delimiter=';')
    for row in reader:
        data.append(row)

def search_operator(query, limit=10):
    query = query.lower()
    results = []

    for row in data:
        nome = row['Razao_Social'].lower()
        score = difflib.SequenceMatcher(None, query, nome).ratio()

        if query in nome or score > 0.5:
            row["score"] = score
            results.append(row)


    results = sorted(results, key=lambda x: x["score"], reverse=True)

    return results[:limit]

@app.route('/search', methods=['GET'])
def search():
    query = request.args.get('query', '').strip()
    if not query:
        return jsonify({"error": "Parâmetro 'query' não informado."}), 400

    results = search_operator(query)
    return jsonify(results)

if __name__ == '__main__':
    app.run(debug=True)
