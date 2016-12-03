import sys
import os
path = os.path.dirname(sys.modules[__name__].__file__)
path = os.path.join(path, '..')
sys.path.insert(0, path)

from app import app

if __name__ == '__main__':
    app.run(debug=True)
