export const handleChange = (self, e) => self.setState({ [e.target.name]: e.target.value });

export const handleChecked = (self, e) => self.setState({ [e.target.name]: e.target.id });

export const handleSimple = (self, e, name) => handleChange(self, {target: {name: name, value: e }});
