import os
import re

directory = r'c:\Users\Utilisateur\Documents\Workshop\src\main\java'
pattern = re.compile(r'\.getResource\("(/)(?!(fxml/))([^"]+\.fxml)"\)')

for root, dirs, files in os.walk(directory):
    for file in files:
        if file.endswith('.java'):
            filepath = os.path.join(root, file)
            with open(filepath, 'r', encoding='utf-8') as f:
                content = f.read()
            
            new_content = pattern.sub(r'.getResource("/fxml/\3")', content)
            
            if new_content != content:
                print(f"Updating {filepath}")
                with open(filepath, 'w', encoding='utf-8') as f:
                    f.write(new_content)
