"use client";
import { useState } from "react";
import { useRouter } from "next/navigation";
import { toast, ToastContainer } from 'react-toastify';
import Image from "next/image";
import '../../styles.css'

const RegistrationPage = () => {
    const [username, setUsername] = useState('');
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [authority, setAuthority] = useState('');
    const [city, setCity] = useState('');
    const [address, setAddress] = useState('');
    const [phoneNumber, setPhoneNumber] = useState('');
    const [error, setError] = useState('');
    const router = useRouter();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError('');
        if (!username) return toast.error('Username is mandatory');
        if (!firstName) return toast.error('first_name is mandatory');
        if (!lastName) return toast.error('lastName is mandatory');
        if (!email) return toast.error('email is mandatory');
        if (!password) return toast.error('Password is mandatory');

        try {
            const response = await fetch('http://localhost:8081/api/v1/auth/signup', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                credentials: 'include',  // Esto es clave para las cookies
                body: JSON.stringify({ username,firstName,lastName,email,password, phoneNumber, address, city, authority}),
            });

            if (!response.ok) {
                throw new Error('Registration failed');
            }

            // Redirecciona
            router.push('login');

        } catch (err) {
            setError('Invalid credentials');
            console.error('Registration error:', err);
        }
    };

    return (
        <div className='main-form-container'>
            <ToastContainer />
            {/* Header */}
            <Image
                onClick={() => { router.push('/'); }}
                className="logo-clickable"
                src="/logo-interfaz.svg"
                alt="interfaz.js logo"
                width={280}
                height={58}
                priority
            />

            {/* Create Section */}

            <div className="form-wrapper">
                <form className="volunteer-form" onSubmit={handleSubmit}>
                    <div className="form-header-inner">
                        <h3 className="form-title">Sign In</h3>

                        {error && <div className="alert alert-danger">{error}</div>}

                        <div className="form-group">
                            <input
                                className="form-label"
                                placeholder="Username"
                                value={username}
                                onChange={(e) => setUsername(e.target.value)}
                                required
                            />
                        </div>

                        <div className="form-group">
                            <input
                                className="form-label"
                                placeholder="First Name"
                                value={firstName}
                                onChange={(e) => setFirstName(e.target.value)}
                                required
                            />
                        </div>

                        <div className="form-group">
                            <input
                                className="form-label"
                                placeholder="Last Name"
                                value={lastName}
                                onChange={(e) => setLastName(e.target.value)}
                                required
                            />
                        </div>

                        <div className="form-group">
                            <input
                                className="form-label"
                                placeholder="email"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                required
                            />
                        </div>


                        <div className="form-group">
                            <input
                                type="password"
                                className="form-label"
                                placeholder="Password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                required
                            />
                        </div>

                        <div className="form-group">
                            <input
                                type="address"
                                className="form-label"
                                placeholder="Adress"
                                value={address}
                                onChange={(e) => setAddress(e.target.value)}
                                required
                            />
                        </div>

                        <div className="form-group">
                            <input
                                type="city"
                                className="form-label"
                                placeholder="City"
                                value={city}
                                onChange={(e) => setCity(e.target.value)}
                                required
                            />
                        </div>


                        <div className="form-group">
                            <select 
                            className="form-control" // Changed from form-label to form-control
                            value={authority}
                            onChange={(e) => setAuthority(e.target.value)}
                            required>
                            <option value="">Select Authority</option>
                            <option value="client">client</option>
                            </select>
                        </div>

                        <div className="form-group">
                            <input
                                type="phoneNumber"
                                className="form-label"
                                placeholder="PhoneNumber"
                                value={phoneNumber}
                                onChange={(e) => setPhoneNumber(e.target.value)}
                                required
                            />
                        </div>

                        <div className="d-grid gap-2 mt-3">
                            <button type="submit" className="submit-button">
                                Sign up
                            </button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default RegistrationPage; 